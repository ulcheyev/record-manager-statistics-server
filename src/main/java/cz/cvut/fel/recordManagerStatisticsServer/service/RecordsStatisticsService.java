package cz.cvut.fel.recordManagerStatisticsServer.service;

import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordListDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordsQueryParams;

public interface RecordsStatisticsService {
    RecordListDto getPersonalRecords(RecordsQueryParams params);
    RecordListDto getInstitutionRecords(RecordsQueryParams params);
    RecordListDto getSystemRecords(RecordsQueryParams params);
}