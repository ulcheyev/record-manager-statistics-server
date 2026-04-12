package cz.cvut.fel.recordManagerStatisticsServer.repository.impl;

import cz.cvut.fel.recordManagerStatisticsServer.repository.InstitutionRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.Institution;
import cz.cvut.kbss.jopa.model.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class DefaultInstitutionRepository extends AbstractRepository<Institution>
        implements InstitutionRepository {

    public DefaultInstitutionRepository(EntityManager em) {
        super(em);
    }
}