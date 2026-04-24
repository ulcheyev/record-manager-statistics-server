package cz.cvut.fel.recordManagerStatisticsServer.repository.model;

import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.FetchType;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@OWLClass(iri = Vocabulary.s_c_Person)
public class RMUser extends AbstractEntity {

    @OWLDataProperty(iri = Vocabulary.s_p_key)
    private String key;

    @OWLDataProperty(iri = Vocabulary.s_p_firstName)
    private String firstName;

    @OWLDataProperty(iri = Vocabulary.s_p_lastName)
    private String lastName;

    @OWLDataProperty(iri = Vocabulary.s_p_accountName)
    private String accountName;

    @OWLObjectProperty(iri = Vocabulary.s_p_is_member_of, fetch = FetchType.EAGER)
    private Institution institution;

    public String getFullName() {
        String first = firstName != null ? firstName : "";
        String last = lastName != null ? lastName : "";
        String full = (first + " " + last).trim();
        return full.isEmpty() ? "" : full;
    }


}