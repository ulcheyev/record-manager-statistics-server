package cz.cvut.fel.recordManagerStatisticsServer.shared.utils;

import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Component
public class RecordFilter {

    public List<RMRecord> byInstitution(List<RMRecord> records, URI institutionUri) {
        return records.stream()
                .filter(r -> r.getInstitution() != null
                        && institutionUri.equals(r.getInstitution().getUri()))
                .toList();
    }

    public List<RMRecord> byAuthor(List<RMRecord> records, URI authorUri) {
        return records.stream()
                .filter(r -> r.getAuthor() != null
                        && authorUri.equals(r.getAuthor().getUri()))
                .toList();
    }

    public Instant earliestCreated(List<RMRecord> records) {
        return extremeCreated(records, Comparator.naturalOrder());
    }

    public Instant latestCreated(List<RMRecord> records) {
        return extremeCreated(records, Comparator.reverseOrder());
    }

    private Instant extremeCreated(List<RMRecord> records, Comparator<Instant> order) {
        return records.stream()
                .map(RMRecord::getDateCreated)
                .filter(d -> d != null)
                .map(d -> d.toInstant())
                .min(order)
                .orElse(null);
    }
}