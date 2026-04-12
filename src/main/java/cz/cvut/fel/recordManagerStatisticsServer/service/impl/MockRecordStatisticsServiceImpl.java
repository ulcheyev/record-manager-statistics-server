package cz.cvut.fel.recordManagerStatisticsServer.service.impl;

import cz.cvut.fel.recordManagerStatisticsServer.dto.Granularity;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.record.*;
import cz.cvut.fel.recordManagerStatisticsServer.service.RecordStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(Constants.Profile.DEMO)
public class MockRecordStatisticsServiceImpl implements RecordStatisticsService {


    @Override
    public GeneralRecordStatsDto getGeneral(StatisticsInterval interval) {
        return null;
    }

    @Override
    public PhaseDistributionDto getPhaseDistribution(StatisticsInterval interval) {
        return null;
    }

    @Override
    public RecordTimelineDto getTimeline(StatisticsInterval interval, Granularity granularity) {
        return null;
    }

    @Override
    public InstitutionStatsDto getByInstitution(StatisticsInterval interval) {
        return null;
    }

    @Override
    public AuthorStatsDto getByAuthor(StatisticsInterval interval) {
        return null;
    }

    @Override
    public FormTemplateUsageDto getByFormTemplate(StatisticsInterval interval) {
        return null;
    }
}