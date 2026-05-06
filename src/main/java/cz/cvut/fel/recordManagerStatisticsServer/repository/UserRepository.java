package cz.cvut.fel.recordManagerStatisticsServer.repository;


import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMUser;

import java.util.Optional;

public interface UserRepository extends Repository<RMUser> {
    Optional<RMUser> findByUsername(String username);
}