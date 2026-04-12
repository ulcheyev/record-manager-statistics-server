package cz.cvut.fel.recordManagerStatisticsServer.repository.impl;

import cz.cvut.fel.recordManagerStatisticsServer.repository.Repository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.AbstractEntity;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.BaseQuery;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractRepository<T extends AbstractEntity>
        implements Repository<T> {

    protected final EntityManager em;
    private final Class<T> type = resolveType();

    @Override
    public List<T> findAll() {
        log.debug("Finding all {}", type.getSimpleName());
        List<T> results = em.createNativeQuery(BaseQuery.FIND_ALL, type)
                .setParameter("type", resolveTypeUri())
                .getResultList();
        log.debug("Found {} of type {}", results.size(), type.getSimpleName());
        return results;
    }

    @Override
    public Optional<T> findByUri(URI uri) {
        log.debug("Finding {} by uri={}", type.getSimpleName(), uri);
        return Optional.ofNullable(em.find(type, uri));
    }

    @Override
    public long count() {
        log.debug("Counting {}", type.getSimpleName());
        long count = em.createNativeQuery(BaseQuery.COUNT_ALL, Long.class)
                .setParameter("type", resolveTypeUri())
                .getSingleResult();
        log.debug("Count of {}: {}", type.getSimpleName(), count);
        return count;
    }


    protected URI resolveTypeUri() {
        OWLClass owlClass = type.getAnnotation(OWLClass.class);
        if (owlClass == null) {
            throw new IllegalStateException(
                    "Class " + type.getName() + " is not annotated with @OWLClass");
        }
        return URI.create(owlClass.iri());
    }

    @SuppressWarnings("unchecked")
    private Class<T> resolveType() {
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }
}