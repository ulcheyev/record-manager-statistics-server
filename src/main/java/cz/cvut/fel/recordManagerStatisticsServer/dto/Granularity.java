package cz.cvut.fel.recordManagerStatisticsServer.dto;

public enum Granularity {

    DAY("10"),
    MONTH("7"),
    YEAR("4");

    private final String substrLength;

    Granularity(String substrLength) {
        this.substrLength = substrLength;
    }

    public String toSparqlSubstr() {
        return substrLength;
    }
}