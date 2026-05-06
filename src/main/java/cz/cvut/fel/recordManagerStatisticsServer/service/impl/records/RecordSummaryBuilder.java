package cz.cvut.fel.recordManagerStatisticsServer.service.impl.records;

import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordSummaryDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordSummaryDtoWithAuthorDetails;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordSummaryDtoWithInstitutionDetails;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.AnswerCounts;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMUser;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Instant;
import java.util.Map;

@Component
public class RecordSummaryBuilder {

    public RecordSummaryDto personal(
            RMRecord record,
            Map<URI, String> templateLabels,
            Map<URI, AnswerCounts> countsByRecord,
            Map<URI, RecordPhase> freshPhases
    ) {
        AnswerCounts counts = counts(record, countsByRecord);

        return RecordSummaryDto.builder()
                .uri(record.getUri())
                .name(recordName(record))
                .phase(phase(record, freshPhases))
                .formTemplateLabel(templateLabel(record, templateLabels))
                .dateCreated(created(record))
                .questions(counts.toQuestionsDto())
                .answers(counts.toAnswersDto())
                .build();
    }

    public RecordSummaryDtoWithAuthorDetails institution(
            RMRecord record,
            Map<URI, String> templateLabels,
            Map<URI, AnswerCounts> countsByRecord,
            Map<URI, RecordPhase> freshPhases
    ) {
        AnswerCounts counts = counts(record, countsByRecord);
        RMUser author = record.getAuthor();

        return RecordSummaryDtoWithAuthorDetails.builder()
                .uri(record.getUri())
                .name(recordName(record))
                .phase(phase(record, freshPhases))
                .formTemplateLabel(templateLabel(record, templateLabels))
                .dateCreated(created(record))
                .questions(counts.toQuestionsDto())
                .answers(counts.toAnswersDto())
                .authorUsername(author == null ? null : author.getAccountName())
                .authorFullName(author == null ? null : author.getFullName())
                .build();
    }

    public RecordSummaryDtoWithInstitutionDetails system(
            RMRecord record,
            Map<URI, String> templateLabels,
            Map<URI, AnswerCounts> countsByRecord,
            Map<URI, RecordPhase> freshPhases
    ) {
        AnswerCounts counts = counts(record, countsByRecord);
        RMUser author = record.getAuthor();

        return RecordSummaryDtoWithInstitutionDetails.builder()
                .uri(record.getUri())
                .name(recordName(record))
                .phase(phase(record, freshPhases))
                .formTemplateLabel(templateLabel(record, templateLabels))
                .dateCreated(created(record))
                .questions(counts.toQuestionsDto())
                .answers(counts.toAnswersDto())
                .authorUsername(author == null ? null : author.getAccountName())
                .authorFullName(author == null ? null : author.getFullName())
                .institutionName(record.getInstitution() == null ? null : record.getInstitution().getName())
                .build();
    }

    private RecordPhase phase(RMRecord record, Map<URI, RecordPhase> freshPhases) {
        if (record.getUri() == null) {
            return record.getPhase();
        }

        return freshPhases.getOrDefault(record.getUri(), record.getPhase());
    }

    private AnswerCounts counts(RMRecord record, Map<URI, AnswerCounts> countsByRecord) {
        return countsByRecord.getOrDefault(
                record.getUri(),
                AnswerCounts.empty(record.getUri())
        );
    }

    private String templateLabel(RMRecord record, Map<URI, String> labels) {
        String formTemplate = record.getFormTemplate();

        if (formTemplate == null || formTemplate.isBlank()) {
            return "Unknown template";
        }

        try {
            URI uri = URI.create(formTemplate);
            return labels.getOrDefault(uri, formTemplate);
        } catch (IllegalArgumentException ex) {
            return formTemplate;
        }
    }

    private Instant created(RMRecord record) {
        return record.getDateCreated() == null
                ? null
                : record.getDateCreated().toInstant();
    }

    private String recordName(RMRecord record) {
        if (record.getName() != null && !record.getName().isBlank()) {
            return record.getName();
        }

        if (record.getKey() != null && !record.getKey().isBlank()) {
            return record.getKey();
        }

        if (record.getUri() == null) {
            return "Unknown record";
        }

        String uri = record.getUri().toString();
        int slash = Math.max(uri.lastIndexOf('/'), uri.lastIndexOf('#'));

        return slash >= 0 ? uri.substring(slash + 1) : uri;
    }
}