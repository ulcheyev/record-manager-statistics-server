package cz.cvut.fel.recordManagerStatisticsServer.repository.model;

import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@OWLClass(iri = Vocabulary.s_c_institution)
public class Institution extends AbstractNamedEntity {
}