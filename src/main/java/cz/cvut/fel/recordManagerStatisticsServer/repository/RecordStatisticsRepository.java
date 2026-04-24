package cz.cvut.fel.recordManagerStatisticsServer.repository;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.RecordQueryCriteria;

import java.util.List;

public interface RecordStatisticsRepository extends Repository<RMRecord> {
    List<RMRecord> findAllByInterval(StatisticsInterval interval);

    List<RMRecord> findAllByCriteria(RecordQueryCriteria criteria);

}