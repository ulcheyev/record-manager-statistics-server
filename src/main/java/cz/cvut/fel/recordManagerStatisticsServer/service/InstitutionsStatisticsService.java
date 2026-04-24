package cz.cvut.fel.recordManagerStatisticsServer.service;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.institutions.InstitutionsStatisticsDto;

public interface InstitutionsStatisticsService {
    InstitutionsStatisticsDto getAllInstitutions(StatisticsInterval interval);
}