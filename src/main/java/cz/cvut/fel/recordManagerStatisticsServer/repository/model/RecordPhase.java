package cz.cvut.fel.recordManagerStatisticsServer.repository.model;

import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Individual;
import lombok.Getter;

@Getter
public enum RecordPhase {

    @Individual(iri = Vocabulary.s_c_open_record_phase)
    OPEN(Vocabulary.s_c_open_record_phase),

    @Individual(iri = Vocabulary.s_c_completed_record_phase)
    COMPLETED(Vocabulary.s_c_completed_record_phase),

    @Individual(iri = Vocabulary.s_c_rejected_record_phase)
    REJECTED(Vocabulary.s_c_rejected_record_phase);


    private final String iri;

    RecordPhase(String iri) {
        this.iri = iri;
    }
}