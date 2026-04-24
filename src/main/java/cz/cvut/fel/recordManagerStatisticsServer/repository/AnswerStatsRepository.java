package cz.cvut.fel.recordManagerStatisticsServer.repository;

import cz.cvut.fel.recordManagerStatisticsServer.dto.AnswerCounts;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;

import java.net.URI;
import java.util.Map;

public interface AnswerStatsRepository {

    Map<URI, AnswerCounts> countByAuthor(StatisticsInterval interval);

    Map<URI, AnswerCounts> countByInstitution(StatisticsInterval interval);

}