package cz.cvut.fel.recordManagerStatisticsServer.service.impl.institutions;

import cz.cvut.fel.recordManagerStatisticsServer.dto.institutions.InstitutionSummaryDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.institutions.InstitutionsStatisticsDto;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.Leaderboard;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class InstitutionsOverviewBuilder {

    public void applyLeaders(
            InstitutionsStatisticsDto.InstitutionsStatisticsDtoBuilder<?, ?> builder,
            List<InstitutionSummaryDto> institutions) {

        Function<InstitutionSummaryDto, String> name = InstitutionSummaryDto::getName;

        builder
                .mostRecordsInstitutionInfo(Leaderboard.by(institutions, InstitutionSummaryDto::getTotalRecords, name, "records"))
                .mostAnswersInstitutionInfo(Leaderboard.by(institutions, InstitutionSummaryDto::getTotalAnswers, name, "answers"))
                .bestCompletionRateInstitutionInfo(Leaderboard.by(institutions, InstitutionSummaryDto::getCompletionRate, name, "completed records"))
                .mostRejectionRateInstitutionInfo(Leaderboard.by(institutions, InstitutionSummaryDto::getRejectionRate, name, "rejected records"))
                .bestAnswerCorrectnessInstitutionInfo(Leaderboard.by(institutions, InstitutionSummaryDto::getCorrectnessRate, name, "correct answers"));
    }
}