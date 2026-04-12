package cz.cvut.fel.recordManagerStatisticsServer.repository.impl;

import cz.cvut.fel.recordManagerStatisticsServer.repository.UserRepository;
import cz.cvut.kbss.jopa.model.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class DefaultUserRepository extends AbstractRepository<cz.cvut.fel.recordManagerStatisticsServer.model.RMUser>
        implements UserRepository {

    public DefaultUserRepository(EntityManager em) {
        super(em);
    }
}