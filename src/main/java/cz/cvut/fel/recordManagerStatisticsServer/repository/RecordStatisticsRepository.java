package cz.cvut.fel.recordManagerStatisticsServer.repository;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.RecordQueryCriteria;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RecordStatisticsRepository extends Repository<RMRecord> {
    List<RMRecord> findAllByInterval(StatisticsInterval interval);

    List<RMRecord> findAllByCriteria(RecordQueryCriteria criteria);

    Map<URI, RecordPhase> findFreshPhasesByRecordUris(Set<URI> recordUris);
}