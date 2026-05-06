package cz.cvut.fel.recordManagerStatisticsServer.service.impl.records;

import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordsQueryParams;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordListFilter {

    public List<RMRecord> apply(List<RMRecord> records, RecordsQueryParams params) {
        if (params == null) {
            return records;
        }

        return records.stream()
                .filter(record -> matchesPhase(record, params))
                .filter(record -> matchesFormTemplate(record, params))
                .toList();
    }

    private boolean matchesPhase(RMRecord record, RecordsQueryParams params) {
        return params.getPhaseFilter() == null
                || record.getPhase() == params.getPhaseFilter();
    }

    private boolean matchesFormTemplate(RMRecord record, RecordsQueryParams params) {
        String filter = params.getFormTemplateFilter();

        if (filter == null || filter.isBlank()) {
            return true;
        }

        String formTemplate = record.getFormTemplate();

        return formTemplate != null
                && formTemplate.toLowerCase().contains(filter.toLowerCase());
    }
}