package cz.cvut.fel.recordManagerStatisticsServer.service.impl.institutions;

import cz.cvut.fel.recordManagerStatisticsServer.dto.institutions.InstitutionSummaryDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.institutions.InstitutionsStatisticsDto;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.Leaderboard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class InstitutionsOverviewBuilder {

    private long totalAnswers(InstitutionSummaryDto institution) {
        return institution.getAnswers() == null ? 0 : institution.getAnswers().totalAnswered();
    }

    private double correctnessRate(InstitutionSummaryDto institution) {
        if (institution.getAnswers() == null || institution.getAnswers().getEvaluable() == null) {
            return 0.0;
        }

        long answered = institution.getAnswers().getEvaluable().getAnswered();
        long correct = institution.getAnswers().getEvaluable().getCorrect();

        return answered == 0 ? 0.0 : (double) correct / answered * 100;
    }

    public void applyLeaders(
            InstitutionsStatisticsDto.InstitutionsStatisticsDtoBuilder<?, ?> builder,
            List<InstitutionSummaryDto> institutions) {

        Function<InstitutionSummaryDto, String> name = InstitutionSummaryDto::getName;

        builder
                .mostRecordsInstitutionInfo(Leaderboard.by(institutions, InstitutionSummaryDto::getTotalRecords, name, "records"))
                .mostAnswersInstitutionInfo(Leaderboard.by(institutions, this::totalAnswers, name, "answers"))
                .bestCompletionRateInstitutionInfo(Leaderboard.by(institutions, InstitutionSummaryDto::getCompletionRate, name, "completed records"))
                .mostRejectionRateInstitutionInfo(Leaderboard.by(institutions, InstitutionSummaryDto::getRejectionRate, name, "rejected records"))
                .bestAnswerCorrectnessInstitutionInfo(Leaderboard.by(institutions, this::correctnessRate, name, "correct answers"));
    }
}