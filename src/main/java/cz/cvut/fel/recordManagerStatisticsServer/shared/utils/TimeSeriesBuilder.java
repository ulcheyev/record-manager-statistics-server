package cz.cvut.fel.recordManagerStatisticsServer.shared.utils;

import cz.cvut.fel.recordManagerStatisticsServer.dto.Granularity;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.TimeSeriesDto;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class TimeSeriesBuilder {

    public TimeSeriesDto build(
            List<RMRecord> records, Granularity granularity, StatisticsInterval interval) {

        Map<String, Map<String, Long>> byPeriod = new LinkedHashMap<>();

        for (RMRecord record : records) {
            if (record.getDateCreated() == null || record.getPhase() == null) continue;
            String period = toPeriodKey(record, granularity);
            String phase = record.getPhase().getIri();
            byPeriod.computeIfAbsent(period, k -> new LinkedHashMap<>())
                    .merge(phase, 1L, Long::sum);
        }

        List<String> labels = new ArrayList<>(byPeriod.keySet());
        List<Long> open = new ArrayList<>();
        List<Long> completed = new ArrayList<>();
        List<Long> rejected = new ArrayList<>();
        List<Long> totals = new ArrayList<>();

        for (String period : labels) {
            Map<String, Long> phases = byPeriod.get(period);
            long o = phases.getOrDefault(RecordPhase.OPEN.getIri(), 0L);
            long c = phases.getOrDefault(RecordPhase.COMPLETED.getIri(), 0L);
            long r = phases.getOrDefault(RecordPhase.REJECTED.getIri(), 0L);
            open.add(o);
            completed.add(c);
            rejected.add(r);
            totals.add(o + c + r);
        }

        Map<String, List<Long>> series = new LinkedHashMap<>();
        series.put("open", open);
        series.put("completed", completed);
        series.put("rejected", rejected);

        return TimeSeriesDto.builder()
                .granularity(granularity)
                .labels(labels)
                .series(series)
                .totals(totals)
                .build();
    }

    private String toPeriodKey(RMRecord record, Granularity granularity) {
        return record.getDateCreated().toInstant()
                .toString()
                .substring(0, Integer.parseInt(granularity.toSparqlSubstr()));
    }
}