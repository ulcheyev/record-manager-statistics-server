package cz.cvut.fel.recordManagerStatisticsServer.config;

import com.github.ledsoft.jopa.spring.transaction.DelegatingEntityManager;
import com.github.ledsoft.jopa.spring.transaction.JopaTransactionManager;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.rdf4j.config.Rdf4jOntoDriverProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile(Constants.Profile.PROD)
@EnableTransactionManagement
public class PersistenceConfig {

    @Value(value = "${persistence.repository.url}")
    private String repositoryUrl;
    @Value(value = "${persistence.username}")
    private String repositoryUsername;
    @Value(value = "${persistence.password}")
    private String repositoryPassword;
    @Value(value = "${persistence.driver}")
    private String driver;

    private EntityManagerFactory emf;

    @Bean
    @Primary
    public EntityManagerFactory getEntityManagerFactory() {
        return this.emf;
    }

    @PreDestroy
    private void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }

    @PostConstruct
    public void init() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.fel.recordManagerStatisticsServer.repository.model");
        properties.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        properties.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, this.repositoryUrl);
        properties.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, this.driver);
        properties.put(JOPAPersistenceProperties.LANG, "en");
        properties.put(JOPAPersistenceProperties.PREFER_MULTILINGUAL_STRING, Boolean.FALSE.toString());
        properties.put(Rdf4jOntoDriverProperties.LOAD_ALL_THRESHOLD, "1");
        properties.put(OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY, Boolean.TRUE.toString());
        if (this.repositoryUsername != null) {
            properties.put(OntoDriverProperties.DATA_SOURCE_USERNAME, this.repositoryUsername);
            properties.put(OntoDriverProperties.DATA_SOURCE_PASSWORD, this.repositoryPassword);
        }
        this.emf = Persistence.createEntityManagerFactory("emf", properties);
    }


    @Bean
    public DelegatingEntityManager entityManager() {
        return new DelegatingEntityManager();
    }


    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf,
                                                         DelegatingEntityManager emProxy) {
        return new JopaTransactionManager(emf, emProxy);
    }


}