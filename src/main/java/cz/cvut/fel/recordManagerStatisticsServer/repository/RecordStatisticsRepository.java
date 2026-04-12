package cz.cvut.fel.recordManagerStatisticsServer.repository;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;

import java.time.Instant;
import java.util.List;

public interface RecordStatisticsRepository extends Repository<RMRecord> {

    long countByPhase(RecordPhase phase, StatisticsInterval interval);

    long countDistinctInstitutions(StatisticsInterval interval);

    long countDistinctAuthors(StatisticsInterval interval);

    Instant findEarliestCreated(StatisticsInterval interval);

    Instant findLatestCreated(StatisticsInterval interval);

    List<RMRecord> findAllByInterval(StatisticsInterval interval);
}