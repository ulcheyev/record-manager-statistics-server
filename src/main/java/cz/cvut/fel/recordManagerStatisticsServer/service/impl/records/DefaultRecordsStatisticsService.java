package cz.cvut.fel.recordManagerStatisticsServer.service.impl.records;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import cz.cvut.fel.recordManagerStatisticsServer.config.security.RequestUserContext;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsLabel;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordListDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordSummaryDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordsQueryParams;
import cz.cvut.fel.recordManagerStatisticsServer.repository.AnswerStatsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.FormTemplateRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.RecordStatisticsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.AnswerCounts;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.service.RecordsStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.exception.StatisticsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultRecordsStatisticsService implements RecordsStatisticsService {

    private final RecordStatisticsRepository recordRepo;
    private final AnswerStatsRepository answerStatsRepo;
    private final FormTemplateRepository formTemplateRepo;
    private final RequestUserContext userContext;

    private final cz.cvut.fel.recordManagerStatisticsServer.shared.utils.RecordFilter scopeFilter;
    private final RecordListFilter listFilter;
    private final RecordSummaryBuilder summaryBuilder;
    private final RecordListBuilder listBuilder;

    @Override
    public RecordListDto getPersonalRecords(RecordsQueryParams params) {
        return getRecords(
                params,
                records -> scopeFilter.byAuthor(records, userContext.getUserUri()),
                summaryBuilder::personal,
                StatisticsLabel.PERSONAL_RECORDS_LIST
        );
    }

    @Override
    public RecordListDto getInstitutionRecords(RecordsQueryParams params) {
        return getRecords(
                params,
                records -> scopeFilter.byInstitution(records, requireCurrentInstitutionUri()),
                summaryBuilder::institution,
                StatisticsLabel.INSTITUTION_RECORDS_LIST
        );
    }

    @Override
    public RecordListDto getSystemRecords(RecordsQueryParams params) {
        return getRecords(
                params,
                Function.identity(),
                summaryBuilder::system,
                StatisticsLabel.SYSTEM_RECORDS_LIST
        );
    }

    private RecordListDto getRecords(
            RecordsQueryParams params,
            Function<List<RMRecord>, List<RMRecord>> scope,
            RecordMapper mapper,
            StatisticsLabel label
    ) {
        StatisticsInterval interval = params.getInterval();

        List<RMRecord> records = recordRepo.findAllByInterval(interval);
        records = scope.apply(records);
        records = listFilter.apply(records, params);

        Map<URI, String> templateLabels = loadTemplateLabels(records);
        Map<URI, AnswerCounts> countsByRecord = answerStatsRepo.countByRecord(interval);
        Map<URI, RecordPhase> freshPhases = recordRepo.findFreshPhasesByRecordUris(
                records.stream()
                        .map(RMRecord::getUri)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet())
        );
        List<RecordSummaryDto> summaries = buildSummaries(
                records,
                templateLabels,
                countsByRecord,
                freshPhases,
                mapper
        );

        return listBuilder.build(
                records,
                summaries,
                templateLabels,
                label,
                interval
        );
    }

    private List<RecordSummaryDto> buildSummaries(
            List<RMRecord> records,
            Map<URI, String> templateLabels,
            Map<URI, AnswerCounts> countsByRecord,
            Map<URI, RecordPhase> freshPhases,
            RecordMapper mapper
    ) {
        return records.stream()
                .map(record -> mapper.map(record, templateLabels, countsByRecord, freshPhases))
                .sorted(Comparator
                        .comparing(
                                RecordSummaryDto::getDateCreated,
                                Comparator.nullsLast(Comparator.reverseOrder())
                        )
                        .thenComparing(
                                RecordSummaryDto::getName,
                                Comparator.nullsLast(String::compareToIgnoreCase)
                        ))
                .toList();
    }

    private Map<URI, String> loadTemplateLabels(List<RMRecord> records) {
        Set<URI> templateUris = records.stream()
                .map(RMRecord::getFormTemplate)
                .filter(value -> value != null && !value.isBlank())
                .map(this::safeUri)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (templateUris.isEmpty()) {
            return Map.of();
        }

        return formTemplateRepo.findLabelsByUris(templateUris);
    }

    private URI safeUri(String value) {
        try {
            return URI.create(value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private URI requireCurrentInstitutionUri() {
        URI uri = userContext.getInstitutionUri();

        if (uri == null) {
            throw new StatisticsNotFoundException("Current user has no institution assigned");
        }

        return uri;
    }

    @FunctionalInterface
    private interface RecordMapper {
        RecordSummaryDto map(
                RMRecord record,
                Map<URI, String> templateLabels,
                Map<URI, AnswerCounts> countsByRecord,
                Map<URI, RecordPhase> freshPhases
        );
    }
}