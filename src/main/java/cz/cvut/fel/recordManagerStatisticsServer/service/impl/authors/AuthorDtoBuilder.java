package cz.cvut.fel.recordManagerStatisticsServer.service.impl.authors;

import cz.cvut.fel.recordManagerStatisticsServer.dto.AnswerCounts;
import cz.cvut.fel.recordManagerStatisticsServer.dto.PhaseCountDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsLabel;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorWithInstitutionDto;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.Institution;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMUser;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.RecordAggregator;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.RecordFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorDtoBuilder {

    private final RecordAggregator aggregator;
    private final RecordFilter recordFilter;

    public AuthorWithInstitutionDto build(
            RMUser user,
            List<RMRecord> records,
            AnswerCounts answerCounts,
            StatisticsLabel label,
            StatisticsInterval interval) {

        PhaseCountDto byPhase = aggregator.toPhaseCount(records);
        Optional<Institution> institution = Optional.ofNullable(user.getInstitution());

        Instant periodFrom = recordFilter.earliestCreated(records);
        Instant periodTo = recordFilter.latestCreated(records);

        return AuthorWithInstitutionDto.builder()
                .username(user.getAccountName())
                .fullName(user.getFullName())
                .label(label)
                .description(buildDescription(user, periodFrom, periodTo))
                .interval(interval)
                .totalRecords(records.size())
                .completionRate(byPhase.completionRate())
                .rejectionRate(byPhase.rejectionRate())
                .totalAnswers(answerCounts.totalAnswers())
                .evaluableAnswers(answerCounts.evaluableAnswers())
                .totalCorrectAnswers(answerCounts.correctAnswers())
                .correctnessRate(answerCounts.correctnessRate())
                .byPhase(byPhase)
                .periodFrom(periodFrom)
                .periodTo(periodTo)
                .institutionUri(institution.map(Institution::getUri).orElse(null))
                .institutionName(institution.map(Institution::getName).orElse(null))
                .build();
    }

    private String buildDescription(RMUser user, Instant from, Instant to) {
        String name = user.getAccountName();
        if (from == null || to == null) {
            return "Performance of author " + name;
        }
        String fromStr = Constants.DateFormats.DATE.format(from);
        String toStr = Constants.DateFormats.DATE.format(to);
        if (fromStr.equals(toStr)) {
            return "Performance of author " + name + " on " + fromStr;
        }
        return "Performance of author " + name + " from " + fromStr + " to " + toStr;
    }


}