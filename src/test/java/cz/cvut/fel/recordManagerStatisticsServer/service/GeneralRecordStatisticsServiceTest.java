package cz.cvut.fel.recordManagerStatisticsServer.service;

import cz.cvut.fel.recordManagerStatisticsServer.config.security.RequestUserContext;
import cz.cvut.fel.recordManagerStatisticsServer.dto.Granularity;
import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.TimeSeriesDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.record.*;
import cz.cvut.fel.recordManagerStatisticsServer.mock.RecordTestFixtures;
import cz.cvut.fel.recordManagerStatisticsServer.repository.RecordStatisticsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import cz.cvut.fel.recordManagerStatisticsServer.service.impl.GeneralRecordStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.RecordAggregator;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.TimeSeriesBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneralRecordStatisticsServiceTest {

    @Mock
    private RecordStatisticsRepository repository;

    @Mock
    private RequestUserContext userContext;

    @Mock
    private RecordAggregator aggregator;

    @Mock
    private TimeSeriesBuilder timeSeriesBuilder;

    @InjectMocks
    private GeneralRecordStatisticsService service;

    private StatisticsInterval interval;
    private List<RMRecord> records;

    @BeforeEach
    void setUp() {
        interval = StatisticsInterval.empty();
        records = RecordTestFixtures.sampleRecords();

        when(userContext.getUserUri())
                .thenReturn(URI.create("http://onto.fel.cvut.cz/ontologies/uzivatel/test-user"));
    }

    @Test
    void getGeneral_returnsCorrectTotalsAndRates() {
        when(repository.countByPhase(RecordPhase.OPEN, interval)).thenReturn(2L);
        when(repository.countByPhase(RecordPhase.COMPLETED, interval)).thenReturn(2L);
        when(repository.countByPhase(RecordPhase.REJECTED, interval)).thenReturn(1L);
        when(repository.countDistinctInstitutions(interval)).thenReturn(2L);
        when(repository.countDistinctAuthors(interval)).thenReturn(2L);
        when(repository.findEarliestCreated(interval)).thenReturn(Instant.ofEpochMilli(1_000_000L));
        when(repository.findLatestCreated(interval)).thenReturn(Instant.ofEpochMilli(1_000_000L));

        GeneralRecordStatsDto result = service.getGeneral(interval);

        assertThat(result.getTotalRecords()).isEqualTo(5);
        assertThat(result.getParticipatingInstitutions()).isEqualTo(2);
        assertThat(result.getEntryClerks()).isEqualTo(2);
        assertThat(result.getAvgRecordsPerInstitution()).isEqualTo(2.5);
        assertThat(result.getAvgRecordsPerAuthor()).isEqualTo(2.5);
        assertThat(result.getPeriodFrom()).isNotNull();
        assertThat(result.getPeriodTo()).isNotNull();
    }

    @Test
    void getGeneral_handlesZeroInstitutions() {
        when(repository.countByPhase(any(), any())).thenReturn(0L);
        when(repository.countDistinctInstitutions(interval)).thenReturn(0L);
        when(repository.countDistinctAuthors(interval)).thenReturn(0L);
        when(repository.findEarliestCreated(interval)).thenReturn(null);
        when(repository.findLatestCreated(interval)).thenReturn(null);

        GeneralRecordStatsDto result = service.getGeneral(interval);

        assertThat(result.getAvgRecordsPerInstitution()).isZero();
        assertThat(result.getAvgRecordsPerAuthor()).isZero();
    }

    @Test
    void getPhaseDistribution_returnsAllThreePhases() {
        when(repository.countByPhase(RecordPhase.OPEN, interval)).thenReturn(2L);
        when(repository.countByPhase(RecordPhase.COMPLETED, interval)).thenReturn(2L);
        when(repository.countByPhase(RecordPhase.REJECTED, interval)).thenReturn(1L);

        PhaseDistributionDto result = service.getPhaseDistribution(interval);

        assertThat(result.getTotal()).isEqualTo(5);
        assertThat(result.getDistribution()).hasSize(3);
        assertThat(result.getDistribution())
                .extracting(PhaseDistributionDto.PhaseSliceDto::getPhase)
                .containsExactlyInAnyOrder(
                        RecordPhase.OPEN, RecordPhase.COMPLETED, RecordPhase.REJECTED);
    }

    @Test
    void getPhaseDistribution_percentagesSumTo100() {
        when(repository.countByPhase(RecordPhase.OPEN, interval)).thenReturn(2L);
        when(repository.countByPhase(RecordPhase.COMPLETED, interval)).thenReturn(2L);
        when(repository.countByPhase(RecordPhase.REJECTED, interval)).thenReturn(1L);

        PhaseDistributionDto result = service.getPhaseDistribution(interval);

        double sum = result.getDistribution().stream()
                .mapToDouble(PhaseDistributionDto.PhaseSliceDto::getPercentage)
                .sum();
        assertThat(sum).isCloseTo(100.0, org.assertj.core.data.Offset.offset(0.01));
    }

    @Test
    void getTimeline_delegatesToTimeSeriesBuilder() {
        TimeSeriesDto timeSeries = TimeSeriesDto.builder()
                .labels(List.of("2023-11"))
                .series(Map.of("open", List.of(2L), "completed", List.of(2L), "rejected", List.of(1L)))
                .totals(List.of(5L))
                .granularity(Granularity.MONTH)
                .build();

        when(repository.findAllByInterval(interval)).thenReturn(records);
        when(timeSeriesBuilder.build(eq(records), eq(Granularity.MONTH), eq(interval)))
                .thenReturn(timeSeries);

        RecordTimelineDto result = service.getTimeline(interval, Granularity.MONTH);

        assertThat(result.getTimeSeries()).isEqualTo(timeSeries);
        assertThat(result.getGranularity()).isEqualTo(Granularity.MONTH);
        verify(timeSeriesBuilder).build(records, Granularity.MONTH, interval);
    }

    @Test
    void getByInstitution_groupsAndSortsByTotalDescending() {
        Map<URI, List<RMRecord>> grouped = new LinkedHashMap<>();
        grouped.put(RecordTestFixtures.INSTITUTION_URI, records.subList(0, 3));
        grouped.put(RecordTestFixtures.INSTITUTION_URI_2, records.subList(3, 5));

        when(repository.findAllByInterval(interval)).thenReturn(records);
        when(aggregator.groupByInstitution(records)).thenReturn(grouped);
        when(aggregator.toPhaseCount(records.subList(0, 3))).thenReturn(
                PhaseCountDto.builder().open(1).completed(2).rejected(0).build());
        when(aggregator.toPhaseCount(records.subList(3, 5))).thenReturn(
                PhaseCountDto.builder().open(1).completed(0).rejected(1).build());

        InstitutionStatsDto result = service.getByInstitution(interval);

        assertThat(result.getInstitutions()).hasSize(2);
        assertThat(result.getInstitutions().get(0).getTotal())
                .isGreaterThanOrEqualTo(result.getInstitutions().get(1).getTotal());
    }

    @Test
    void getByAuthor_populatesFullNameAndUsername() {
        Map<URI, List<RMRecord>> grouped = new LinkedHashMap<>();
        grouped.put(RecordTestFixtures.AUTHOR_URI, records.subList(0, 3));

        when(repository.findAllByInterval(interval)).thenReturn(records);
        when(aggregator.groupByAuthor(records)).thenReturn(grouped);
        when(aggregator.toPhaseCount(any())).thenReturn(
                PhaseCountDto.builder().open(1).completed(2).rejected(0).build());

        AuthorStatsDto result = service.getByAuthor(interval);

        assertThat(result.getAuthors()).hasSize(1);
        AuthorStatsDto.AuthorRecordStatsDto author = result.getAuthors().get(0);
        assertThat(author.getFullName()).isEqualTo("John Doe");
        assertThat(author.getUsername()).isEqualTo("jdoe");
        assertThat(author.getInstitutionName()).isEqualTo("General Hospital");
    }

    @Test
    void getByFormTemplate_calculatesPercentagesCorrectly() {
        when(repository.findAllByInterval(interval)).thenReturn(records);
        when(aggregator.groupByFormTemplate(records))
                .thenReturn(Map.of("form-a", 3L, "form-b", 2L));

        FormTemplateUsageDto result = service.getByFormTemplate(interval);

        assertThat(result.getTotal()).isEqualTo(5);
        assertThat(result.getTemplates()).hasSize(2);

        FormTemplateUsageDto.TemplateSliceDto formA = result.getTemplates().stream()
                .filter(t -> t.getTemplateUri().equals("form-a"))
                .findFirst().orElseThrow();

        assertThat(formA.getPercentage()).isCloseTo(60.0,
                org.assertj.core.data.Offset.offset(0.01));
    }

    @Test
    void getByFormTemplate_sortsByCountDescending() {
        when(repository.findAllByInterval(interval)).thenReturn(records);
        when(aggregator.groupByFormTemplate(records))
                .thenReturn(Map.of("form-a", 3L, "form-b", 2L));

        FormTemplateUsageDto result = service.getByFormTemplate(interval);

        assertThat(result.getTemplates().get(0).getCount())
                .isGreaterThanOrEqualTo(result.getTemplates().get(1).getCount());
    }
}