package cz.cvut.fel.recordManagerStatisticsServer.service.impl;

import cz.cvut.fel.recordManagerStatisticsServer.dto.CategoryCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.RecordStatsDto;
import cz.cvut.fel.recordManagerStatisticsServer.service.RecordStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile(Constants.Profile.DEMO)
public class MockRecordStatisticsServiceImpl implements RecordStatisticsService {

    @Override
    public RecordStatsDto getRecordStats() {
        return RecordStatsDto.builder()
                .total(14872L)
                .byCategory(List.of(
                        CategoryCountDto.builder().category("Incident report").count(4821).build(),
                        CategoryCountDto.builder().category("Audit record").count(3240).build(),
                        CategoryCountDto.builder().category("Safety check").count(2890).build(),
                        CategoryCountDto.builder().category("Maintenance").count(1980).build(),
                        CategoryCountDto.builder().category("Training log").count(1240).build(),
                        CategoryCountDto.builder().category("Other").count(701).build()
                ))
                .build();
    }
}