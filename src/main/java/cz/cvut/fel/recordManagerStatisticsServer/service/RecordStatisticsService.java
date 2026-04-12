package cz.cvut.fel.recordManagerStatisticsServer.service;

import cz.cvut.fel.recordManagerStatisticsServer.dto.Granularity;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.record.*;

public interface RecordStatisticsService {

    GeneralRecordStatsDto getGeneral(StatisticsInterval interval);

    PhaseDistributionDto getPhaseDistribution(StatisticsInterval interval);

    RecordTimelineDto getTimeline(StatisticsInterval interval, Granularity granularity);

    InstitutionStatsDto getByInstitution(StatisticsInterval interval);

    AuthorStatsDto getByAuthor(StatisticsInterval interval);

    FormTemplateUsageDto getByFormTemplate(StatisticsInterval interval);

}