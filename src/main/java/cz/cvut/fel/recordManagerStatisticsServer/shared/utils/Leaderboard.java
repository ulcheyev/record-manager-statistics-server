package cz.cvut.fel.recordManagerStatisticsServer.shared.utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

public final class Leaderboard {

    private Leaderboard() {
    }

    public static <T> String by(
            List<T> items,
            ToLongFunction<T> metric,
            Function<T, String> name,
            String propertyLabel) {

        return items.stream()
                .filter(item -> metric.applyAsLong(item) > 0)
                .max(Comparator.comparingLong(metric))
                .map(winner -> format(name.apply(winner), metric.applyAsLong(winner)))
                .orElse(noneMessage(propertyLabel));
    }

    public static <T> String by(
            List<T> items,
            ToDoubleFunction<T> metric,
            Function<T, String> name,
            String propertyLabel) {

        return items.stream()
                .filter(item -> metric.applyAsDouble(item) > 0.0)
                .max(Comparator.comparingDouble(metric))
                .map(winner -> format(name.apply(winner), metric.applyAsDouble(winner)))
                .orElse(noneMessage(propertyLabel));
    }

    private static String format(String name, long value) {
        return "%s (%d)".formatted(name, value);
    }

    private static String format(String name, double value) {
        return "%s (%.1f%%)".formatted(name, value);
    }

    private static String noneMessage(String propertyLabel) {
        return "No entries with " + propertyLabel + " yet";
    }
}