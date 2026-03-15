package cz.cvut.fel.recordManagerStatisticsServer.service.impl;

import cz.cvut.fel.recordManagerStatisticsServer.dto.RecordStatsDto;
import cz.cvut.fel.recordManagerStatisticsServer.service.RecordStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;



@Service
@Profile(Constants.Profile.PROD)
@RequiredArgsConstructor
public class DefaultRecordStatisticsService implements RecordStatisticsService {

    @Override
    public RecordStatsDto getRecordStats() {
        return null;
    }
}
