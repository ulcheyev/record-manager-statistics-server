package cz.cvut.fel.recordManagerStatisticsServer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsInterval {

    private LocalDate from;
    private LocalDate to;

    public static StatisticsInterval empty() {
        return new StatisticsInterval(null, null);
    }

    public static StatisticsInterval of(LocalDate from, LocalDate to) {
        return new StatisticsInterval(from, to);
    }

    public boolean hasFrom() {
        return from != null;
    }

    public boolean hasTo() {
        return to != null;
    }

    public boolean isEmpty() {
        return from == null && to == null;
    }

    @Override
    public String toString() {
        return "StatisticsInterval(from=" + from + ", to=" + to + ")";
    }
}