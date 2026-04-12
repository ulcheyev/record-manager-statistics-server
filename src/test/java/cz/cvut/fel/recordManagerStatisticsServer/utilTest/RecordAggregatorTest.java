package cz.cvut.fel.recordManagerStatisticsServer.utilTest;

import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.mock.RecordTestFixtures;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.RecordAggregator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static cz.cvut.fel.recordManagerStatisticsServer.mock.RecordTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class RecordAggregatorTest {

    private RecordAggregator aggregator;
    private List<RMRecord> records;

    @BeforeEach
    void setUp() {
        aggregator = new RecordAggregator();
        records = RecordTestFixtures.sampleRecords();
    }

    @Test
    void groupByInstitution_groupsCorrectly() {
        Map<URI, List<RMRecord>> result = aggregator.groupByInstitution(records);

        assertThat(result).hasSize(2);
        assertThat(result.get(INSTITUTION_URI)).hasSize(3);
        assertThat(result.get(INSTITUTION_URI_2)).hasSize(2);
    }

    @Test
    void groupByInstitution_excludesRecordsWithNullInstitution() {
        RMRecord nullInst = new RMRecord();
        nullInst.setUri(URI.create("http://record/null"));
        nullInst.setPhase(RecordPhase.OPEN);

        List<RMRecord> withNull = new java.util.ArrayList<>(records);
        withNull.add(nullInst);

        Map<URI, List<RMRecord>> result = aggregator.groupByInstitution(withNull);

        assertThat(result).hasSize(2);
    }

    @Test
    void groupByAuthor_groupsCorrectly() {
        Map<URI, List<RMRecord>> result = aggregator.groupByAuthor(records);

        assertThat(result).hasSize(2);
        assertThat(result.get(AUTHOR_URI)).hasSize(3);
        assertThat(result.get(AUTHOR_URI_2)).hasSize(2);
    }

    @Test
    void groupByFormTemplate_countsCorrectly() {
        Map<String, Long> result = aggregator.groupByFormTemplate(records);

        assertThat(result).hasSize(2);
        assertThat(result.get("form-a")).isEqualTo(3L);
        assertThat(result.get("form-b")).isEqualTo(2L);
    }

    @Test
    void groupByFormTemplate_excludesNullTemplates() {
        RMRecord nullTemplate = new RMRecord();
        nullTemplate.setUri(URI.create("http://record/null"));

        List<RMRecord> withNull = new java.util.ArrayList<>(records);
        withNull.add(nullTemplate);

        Map<String, Long> result = aggregator.groupByFormTemplate(withNull);

        assertThat(result).hasSize(2);
    }

    @Test
    void toPhaseCount_countsCorrectly() {
        PhaseCountDto result = aggregator.toPhaseCount(records);

        assertThat(result.getOpen()).isEqualTo(2);
        assertThat(result.getCompleted()).isEqualTo(2);
        assertThat(result.getRejected()).isEqualTo(1);
        assertThat(result.total()).isEqualTo(5);
    }

    @Test
    void toPhaseCount_returnsZerosForEmptyList() {
        PhaseCountDto result = aggregator.toPhaseCount(List.of());

        assertThat(result.getOpen()).isZero();
        assertThat(result.getCompleted()).isZero();
        assertThat(result.getRejected()).isZero();
        assertThat(result.total()).isZero();
    }
}