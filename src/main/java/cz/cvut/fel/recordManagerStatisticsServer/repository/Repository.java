package cz.cvut.fel.recordManagerStatisticsServer.repository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    List<T> findAll();

    Optional<T> findByUri(URI uri);

    long count();
}