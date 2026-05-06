package cz.cvut.fel.recordManagerStatisticsServer.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatisticsLabel {

    PERSONAL_OVERVIEW("Overview of personal statistics"),
    INSTITUTION_OVERVIEW("Statistics for your institution"),
    SYSTEM_OVERVIEW("System-wide statistics"),
    SYSTEM_INSTITUTIONS_OVERVIEW("Institutions across the system"),

    INSTITUTION_AUTHORS_OVERVIEW("Authors in your institution"),
    SYSTEM_AUTHORS_OVERVIEW("Authors across the system"),
    INSTITUTION_AUTHORS_SCATTER("Author performance in your institution"),
    SYSTEM_AUTHORS_SCATTER("Author performance across the system"),

    PERSONAL_RECORDS_OVERVIEW("My records overview"),
    INSTITUTION_RECORDS_OVERVIEW("Records in your institution"),
    SYSTEM_RECORDS_OVERVIEW("Records across the system"),
    PERSONAL_RECORDS_LIST("My records list"),
    INSTITUTION_RECORDS_LIST("Records in your institution"),
    SYSTEM_RECORDS_LIST("All records in the system"),

    RECORD_DETAIL("Record statistics");

    private final String description;

    StatisticsLabel(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}