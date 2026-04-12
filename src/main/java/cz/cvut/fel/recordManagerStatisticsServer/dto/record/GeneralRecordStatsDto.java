package cz.cvut.fel.recordManagerStatisticsServer.dto.record;

import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class GeneralRecordStatsDto {

    private StatisticsInterval interval;
    private PhaseCountDto byPhase;

    private long totalRecords;
    private double completionRate;
    private double rejectionRate;

    private Long participatingInstitutions;
    private Long entryClerks;

    private Instant periodFrom;
    private Instant periodTo;

    private Double avgRecordsPerInstitution;
    private Double avgRecordsPerAuthor;
}