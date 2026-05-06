package cz.cvut.fel.recordManagerStatisticsServer.service.impl.authors;

import cz.cvut.fel.recordManagerStatisticsServer.config.security.RequestUserContext;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsLabel;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorWithInstitutionDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorsOverviewDto;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.Leaderboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AuthorsOverviewBuilder {

    private final RequestUserContext userContext;

    private long totalAnswers(AuthorWithInstitutionDto author) {
        return author.getAnswers() == null ? 0 : author.getAnswers().totalAnswered();
    }

    private double correctnessRate(AuthorWithInstitutionDto author) {
        if (author.getAnswers() == null || author.getAnswers().getEvaluable() == null) {
            return 0.0;
        }

        long answered = author.getAnswers().getEvaluable().getAnswered();
        long correct = author.getAnswers().getEvaluable().getCorrect();

        return answered == 0 ? 0.0 : (double) correct / answered * 100;
    }


    public AuthorsOverviewDto build(
            List<AuthorWithInstitutionDto> authors,
            StatisticsLabel label,
            StatisticsInterval interval) {

        Function<AuthorWithInstitutionDto, String> name = AuthorWithInstitutionDto::getUsername;

        return AuthorsOverviewDto.builder()
                .label(label)
                .description(label.getDescription())
                .interval(interval)
                .totalAuthors(authors.size())
                .mostRecordsAuthorInfo(Leaderboard.by(authors, AuthorWithInstitutionDto::getTotalRecords, name, "records"))
                .mostAnswersAuthorInfo(Leaderboard.by(authors, this::totalAnswers, name, "answers"))
                .bestCompletionRateInfo(Leaderboard.by(authors, AuthorWithInstitutionDto::getCompletionRate, name, "completed records"))
                .mostRejectionRateInfo(Leaderboard.by(authors, AuthorWithInstitutionDto::getRejectionRate, name, "rejected records"))
                .bestAnswerCorrectnessInfo(Leaderboard.by(authors, this::correctnessRate, name, "correct answers"))
                .build();
    }


    private String buildDescription(List<AuthorWithInstitutionDto> authors, StatisticsLabel label) {
        int count = authors.size();
        return switch (label) {
            case SYSTEM_AUTHORS_OVERVIEW -> count + " authors contributed records across the system";
            case INSTITUTION_AUTHORS_OVERVIEW ->
                    count + " authors contributed records in institution " + userContext.getInstitutionName();
            default -> "";
        };
    }

}