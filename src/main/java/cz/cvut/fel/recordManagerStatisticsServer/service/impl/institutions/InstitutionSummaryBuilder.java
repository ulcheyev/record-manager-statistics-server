package cz.cvut.fel.recordManagerStatisticsServer.service.impl.institutions;

import cz.cvut.fel.recordManagerStatisticsServer.dto.AnswerCounts;
import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.institutions.InstitutionSummaryDto;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.Institution;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.RecordAggregator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InstitutionSummaryBuilder {

    private final RecordAggregator aggregator;

    public InstitutionSummaryDto build(
            Institution institution,
            List<RMRecord> records,
            AnswerCounts answerCounts) {

        PhaseCountDto byPhase = aggregator.toPhaseCount(records);

        return InstitutionSummaryDto.builder()
                .uri(institution.getUri())
                .name(institution.getName())
                .totalRecords(records.size())
                .authorCount(aggregator.groupByAuthor(records).size())
                .completionRate(byPhase.completionRate())
                .rejectionRate(byPhase.rejectionRate())
                .totalAnswers(answerCounts.totalAnswers())
                .evaluableAnswers(answerCounts.evaluableAnswers())
                .totalCorrectAnswers(answerCounts.correctAnswers())
                .correctnessRate(answerCounts.correctnessRate())
                .correctnessRate(answerCounts.correctnessRate())
                .build();
    }
}