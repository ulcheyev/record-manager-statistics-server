package cz.cvut.fel.recordManagerStatisticsServer.repository.model;

import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@OWLClass(iri = Vocabulary.s_c_record)
public class RMRecord extends AbstractEntity {

    @OWLDataProperty(iri = Vocabulary.s_p_key)
    private String key;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_author, fetch = FetchType.EAGER)
    private RMUser author;

    @OWLObjectProperty(iri = Vocabulary.s_p_was_treated_at, fetch = FetchType.EAGER)
    private Institution institution;

    @OWLDataProperty(iri = Vocabulary.s_p_created)
    private Date dateCreated;

    @OWLDataProperty(iri = Vocabulary.s_p_has_form_template)
    private String formTemplate;

    @Enumerated(EnumType.OBJECT_ONE_OF)
    @OWLObjectProperty(iri = Vocabulary.s_p_has_phase, fetch = FetchType.EAGER)
    private RecordPhase phase;

}