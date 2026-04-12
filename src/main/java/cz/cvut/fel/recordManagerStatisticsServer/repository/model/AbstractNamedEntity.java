package cz.cvut.fel.recordManagerStatisticsServer.repository.model;

import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractNamedEntity extends AbstractEntity {

    @OWLDataProperty(iri = Vocabulary.s_p_key)
    private String key;

    @OWLAnnotationProperty(iri = Vocabulary.s_p_label)
    private String name;
}