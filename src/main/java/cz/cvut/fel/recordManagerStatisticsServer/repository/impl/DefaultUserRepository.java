package cz.cvut.fel.recordManagerStatisticsServer.repository.impl;

import cz.cvut.fel.recordManagerStatisticsServer.repository.UserRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMUser;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.UserQuery;
import cz.cvut.kbss.jopa.model.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class DefaultUserRepository extends AbstractRepository<RMUser>
        implements UserRepository {

    public DefaultUserRepository(EntityManager em) {
        super(em);
    }

    @Override
    public Optional<RMUser> findByUsername(String username) {
        log.debug("findByUsername username={}", username);
        List<RMUser> results = em.createNativeQuery(
                        UserQuery.FIND_BY_USERNAME.formatted(
                                Vocabulary.s_c_Person,
                                Vocabulary.s_p_accountName,
                                username
                        ), RMUser.class)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.getFirst());
    }
}