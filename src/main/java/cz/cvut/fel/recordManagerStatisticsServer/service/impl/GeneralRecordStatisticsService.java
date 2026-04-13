package cz.cvut.fel.recordManagerStatisticsServer.service.impl;

import cz.cvut.fel.recordManagerStatisticsServer.config.security.RequestUserContext;
import cz.cvut.fel.recordManagerStatisticsServer.dto.Granularity;
import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.record.*;
import cz.cvut.fel.recordManagerStatisticsServer.model.RMUser;
import cz.cvut.fel.recordManagerStatisticsServer.repository.FormTemplateRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.RecordStatisticsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.Institution;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import cz.cvut.fel.recordManagerStatisticsServer.service.RecordStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.RecordAggregator;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.TimeSeriesBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeneralRecordStatisticsService implements RecordStatisticsService {

    private final RecordStatisticsRepository repository;
    private final FormTemplateRepository formTemplateRepository;
    private final RequestUserContext userContext;
    private final RecordAggregator aggregator;
    private final TimeSeriesBuilder timeSeriesBuilder;

    @Override
    public GeneralRecordStatsDto getGeneral(StatisticsInterval interval) {
        log.info("getGeneral user={} interval={}", userContext.getUserUri(), interval);

        PhaseCountDto byPhase = fetchPhaseCount(interval);
        long total = byPhase.total();
        long institutions = repository.countDistinctInstitutions(interval);
        long authors = repository.countDistinctAuthors(interval);

        return GeneralRecordStatsDto.builder()
                .interval(interval)
                .byPhase(byPhase)
                .totalRecords(total)
                .completionRate(byPhase.completionRate())
                .rejectionRate(byPhase.rejectionRate())
                .participatingInstitutions(institutions)
                .entryClerks(authors)
                .periodFrom(repository.findEarliestCreated(interval))
                .periodTo(repository.findLatestCreated(interval))
                .avgRecordsPerInstitution(safeDivide(total, institutions))
                .avgRecordsPerAuthor(safeDivide(total, authors))
                .build();
    }

    @Override
    public PhaseDistributionDto getPhaseDistribution(StatisticsInterval interval) {
        log.info("getPhaseDistribution user={} interval={}", userContext.getUserUri(), interval);

        PhaseCountDto counts = fetchPhaseCount(interval);
        long total = counts.total();

        return PhaseDistributionDto.builder()
                .interval(interval)
                .total(total)
                .distribution(List.of(
                        toSlice(RecordPhase.COMPLETED, counts.getCompleted(), total),
                        toSlice(RecordPhase.REJECTED, counts.getRejected(), total),
                        toSlice(RecordPhase.OPEN, counts.getOpen(), total)
                ))
                .build();
    }

    @Override
    public RecordTimelineDto getTimeline(StatisticsInterval interval, Granularity granularity) {
        log.info("getTimeline user={} granularity={} interval={}",
                userContext.getUserUri(), granularity, interval);

        List<RMRecord> records = repository.findAllByInterval(interval);

        return RecordTimelineDto.builder()
                .interval(interval)
                .granularity(granularity)
                .timeSeries(timeSeriesBuilder.build(records, granularity, interval))
                .build();
    }

    @Override
    public InstitutionStatsDto getByInstitution(StatisticsInterval interval) {
        log.info("getByInstitution user={} interval={}", userContext.getUserUri(), interval);

        List<RMRecord> records = repository.findAllByInterval(interval);

        List<InstitutionStatsDto.InstitutionRecordStatsDto> institutions =
                aggregator.groupByInstitution(records).entrySet().stream()
                        .map(e -> toInstitutionDto(e.getKey(), e.getValue()))
                        .sorted(Comparator.comparingLong(
                                InstitutionStatsDto.InstitutionRecordStatsDto::getTotal).reversed())
                        .collect(Collectors.toList());

        return InstitutionStatsDto.builder()
                .interval(interval)
                .institutions(institutions)
                .build();
    }

    @Override
    public AuthorStatsDto getByAuthor(StatisticsInterval interval) {
        log.info("getByAuthor user={} interval={}", userContext.getUserUri(), interval);

        List<RMRecord> records = repository.findAllByInterval(interval);

        List<AuthorStatsDto.AuthorRecordStatsDto> authors =
                aggregator.groupByAuthor(records).entrySet().stream()
                        .map(e -> toAuthorDto(e.getKey(), e.getValue()))
                        .sorted(Comparator.comparingLong(
                                AuthorStatsDto.AuthorRecordStatsDto::getTotal).reversed())
                        .collect(Collectors.toList());

        return AuthorStatsDto.builder()
                .interval(interval)
                .authors(authors)
                .build();
    }

    @Override
    public FormTemplateUsageDto getByFormTemplate(StatisticsInterval interval) {
        log.info("getByFormTemplate user={} interval={}", userContext.getUserUri(), interval);

        List<RMRecord> records = repository.findAllByInterval(interval);

        Map<String, Long> byTemplate = records.stream()
                .filter(r -> r.getFormTemplate() != null)
                .collect(Collectors.groupingBy(
                        RMRecord::getFormTemplate, Collectors.counting()));

        long total = byTemplate.values().stream().mapToLong(Long::longValue).sum();

        Set<URI> templateUris = byTemplate.keySet().stream()
                .map(URI::create)
                .collect(Collectors.toSet());

        Map<URI, String> labels = formTemplateRepository.findLabelsByUris(templateUris);

        List<FormTemplateUsageDto.TemplateSliceDto> templates = byTemplate.entrySet().stream()
                .map(e -> {
                    URI uri   = URI.create(e.getKey());
                    long count = e.getValue();
                    return FormTemplateUsageDto.TemplateSliceDto.builder()
                            .templateUri(e.getKey())
                            .templateLabel(labels.getOrDefault(uri, e.getKey()))
                            .count(count)
                            .percentage(safeDivide(count, total) * 100)
                            .build();
                })
                .sorted(Comparator.comparingLong(
                        FormTemplateUsageDto.TemplateSliceDto::getCount).reversed())
                .collect(Collectors.toList());

        return FormTemplateUsageDto.builder()
                .interval(interval)
                .total(total)
                .templates(templates)
                .build();
    }


    private PhaseCountDto fetchPhaseCount(StatisticsInterval interval) {
        return PhaseCountDto.builder()
                .open(repository.countByPhase(RecordPhase.OPEN, interval))
                .completed(repository.countByPhase(RecordPhase.COMPLETED, interval))
                .rejected(repository.countByPhase(RecordPhase.REJECTED, interval))
                .build();
    }


    private InstitutionStatsDto.InstitutionRecordStatsDto toInstitutionDto(
            URI uri, List<RMRecord> records) {
        Institution inst = records.get(0).getInstitution();
        PhaseCountDto byPhase = aggregator.toPhaseCount(records);
        return InstitutionStatsDto.InstitutionRecordStatsDto.builder()
                .uri(uri)
                .name(inst.getName() != null ? inst.getName() : uri.toString())
                .byPhase(byPhase)
                .total(byPhase.total())
                .completionRate(byPhase.completionRate())
                .rejectionRate(byPhase.rejectionRate())
                .build();
    }

    private AuthorStatsDto.AuthorRecordStatsDto toAuthorDto(
            URI uri, List<RMRecord> records) {
        RMUser author = records.getFirst().getAuthor();
        PhaseCountDto byPhase = aggregator.toPhaseCount(records);
        return AuthorStatsDto.AuthorRecordStatsDto.builder()
                .uri(uri)
                .fullName(author.getFullName())
                .username(author.getAccountName())
                .institutionName(author.getInstitution() != null
                        ? author.getInstitution().getName()
                        : null)
                .byPhase(byPhase)
                .total(byPhase.total())
                .completionRate(byPhase.completionRate())
                .rejectionRate(byPhase.rejectionRate())
                .build();
    }

    private PhaseDistributionDto.PhaseSliceDto toSlice(
            RecordPhase phase, long count, long total) {
        return PhaseDistributionDto.PhaseSliceDto.builder()
                .phase(phase)
                .count(count)
                .percentage(total > 0 ? (double) count / total * 100 : 0.0)
                .build();
    }

    private FormTemplateUsageDto.TemplateSliceDto toTemplateSlice(
            String templateUri, long count, long total) {
        return FormTemplateUsageDto.TemplateSliceDto.builder()
                .templateUri(templateUri)
                .count(count)
                .percentage(total > 0 ? (double) count / total * 100 : 0.0)
                .build();
    }


    private double safeDivide(long numerator, long denominator) {
        return denominator > 0 ? (double) numerator / denominator : 0.0;
    }
}