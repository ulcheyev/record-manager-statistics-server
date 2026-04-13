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

    @Value("${persistence.repository.url}")
    private String repositoryUrl;

    @Value("${persistence.formgen.url}")
    private String formgenRepositoryUrl;

    @Value("${persistence.username}")
    private String username;

    @Value("${persistence.password}")
    private String password;

    @Value("${persistence.driver}")
    private String driver;

    private EntityManagerFactory emf;
    private EntityManagerFactory formgenEmf;

    @PostConstruct
    public void init() {
        this.emf        = createEntityManagerFactory("emf",        repositoryUrl);
        this.formgenEmf = createEntityManagerFactory("formgenEmf", formgenRepositoryUrl);
    }

    @PreDestroy
    private void close() {
        closeIfOpen(emf);
        closeIfOpen(formgenEmf);
    }

    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory() {
        return emf;
    }

    @Bean
    @Primary
    public DelegatingEntityManager entityManager() {
        return new DelegatingEntityManager();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() {
        return new JopaTransactionManager(emf, entityManager());
    }


    @Bean(name = "formgenEntityManagerFactory")
    public EntityManagerFactory formgenEntityManagerFactory() {
        return formgenEmf;
    }

    @Bean(name = "formgenEntityManager")
    public DelegatingEntityManager formgenEntityManager() {
        return new DelegatingEntityManager();
    }

    @Bean(name = "formgenTransactionManager")
    public PlatformTransactionManager formgenTransactionManager() {
        return new JopaTransactionManager(formgenEmf, formgenEntityManager());
    }

    private EntityManagerFactory createEntityManagerFactory(String name, String url) {
        final Map<String, String> props = new HashMap<>();
        props.put(JOPAPersistenceProperties.SCAN_PACKAGE,
                "cz.cvut.fel.recordManagerStatisticsServer.repository.model");
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER,
                JOPAPersistenceProvider.class.getName());
        props.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, url);
        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, driver);
        props.put(JOPAPersistenceProperties.LANG, "en");
        props.put(JOPAPersistenceProperties.PREFER_MULTILINGUAL_STRING,
                Boolean.FALSE.toString());
        props.put(Rdf4jOntoDriverProperties.LOAD_ALL_THRESHOLD, "1");
        props.put(OntoDriverProperties.USE_TRANSACTIONAL_ONTOLOGY,
                Boolean.TRUE.toString());
        if (username != null && !username.isBlank()) {
            props.put(OntoDriverProperties.DATA_SOURCE_USERNAME, username);
            props.put(OntoDriverProperties.DATA_SOURCE_PASSWORD, password);
        }
        return Persistence.createEntityManagerFactory(name, props);
    }

    private void closeIfOpen(EntityManagerFactory factory) {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}