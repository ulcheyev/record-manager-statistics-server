package cz.cvut.fel.recordManagerStatisticsServer.shared.utils;

import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RecordAggregator {

    public Map<URI, List<RMRecord>> groupByInstitution(List<RMRecord> records) {
        return records.stream()
                .filter(r -> r.getInstitution() != null
                        && r.getInstitution().getUri() != null)
                .collect(Collectors.groupingBy(r -> r.getInstitution().getUri()));
    }

    public Map<URI, List<RMRecord>> groupByAuthor(List<RMRecord> records) {
        return records.stream()
                .filter(r -> r.getAuthor() != null
                        && r.getAuthor().getUri() != null)
                .collect(Collectors.groupingBy(r -> r.getAuthor().getUri()));
    }

    public Map<String, Long> groupByFormTemplate(List<RMRecord> records) {
        return records.stream()
                .filter(r -> r.getFormTemplate() != null)
                .collect(Collectors.groupingBy(
                        RMRecord::getFormTemplate, Collectors.counting()));
    }

    public PhaseCountDto toPhaseCount(List<RMRecord> records) {
        return PhaseCountDto.builder()
                .open(countByPhase(records, RecordPhase.OPEN))
                .completed(countByPhase(records, RecordPhase.COMPLETED))
                .rejected(countByPhase(records, RecordPhase.REJECTED))
                .build();
    }

    private long countByPhase(List<RMRecord> records, RecordPhase phase) {
        return records.stream()
                .filter(r -> r.getPhase() == phase)
                .count();
    }
}