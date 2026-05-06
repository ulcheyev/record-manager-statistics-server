package cz.cvut.fel.recordManagerStatisticsServer.repository;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.AnswerCounts;

import java.net.URI;
import java.util.Map;

public interface AnswerStatsRepository {

    Map<URI, AnswerCounts> countByAuthor(StatisticsInterval interval);

    Map<URI, AnswerCounts> countByInstitution(StatisticsInterval interval);

    Map<URI, AnswerCounts> countByRecord(StatisticsInterval interval);
}