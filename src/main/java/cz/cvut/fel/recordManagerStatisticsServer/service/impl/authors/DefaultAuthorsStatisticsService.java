package cz.cvut.fel.recordManagerStatisticsServer.service.impl.authors;

import cz.cvut.fel.recordManagerStatisticsServer.config.security.RequestUserContext;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.AnswerCounts;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsLabel;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.*;
import cz.cvut.fel.recordManagerStatisticsServer.repository.AnswerStatsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.RecordStatisticsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.UserRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMUser;
import cz.cvut.fel.recordManagerStatisticsServer.repository.query.RecordQueryCriteria;
import cz.cvut.fel.recordManagerStatisticsServer.service.AuthorsStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.exception.StatisticsNotFoundException;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.RecordAggregator;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.RecordFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultAuthorsStatisticsService implements AuthorsStatisticsService {

    private final RecordStatisticsRepository recordRepo;
    private final UserRepository userRepo;
    private final AnswerStatsRepository answerStatsRepo;
    private final RequestUserContext userContext;
    private final RecordAggregator aggregator;
    private final RecordFilter recordFilter;
    private final AuthorDtoBuilder authorBuilder;
    private final AuthorsOverviewBuilder overviewBuilder;

    @Override
    public AuthorsOverviewDto getAllAuthorsOverview(StatisticsInterval interval) {
        return overviewBuilder.build(
                authorsFromSystem(interval),
                StatisticsLabel.SYSTEM_AUTHORS_OVERVIEW,
                interval);
    }

    @Override
    public AuthorsOverviewDto getInstitutionAuthorsOverview(StatisticsInterval interval) {
        return overviewBuilder.build(
                authorsFromCurrentInstitution(interval),
                StatisticsLabel.INSTITUTION_AUTHORS_OVERVIEW,
                interval);
    }

    @Override
    public AuthorsStatisticsDto getAllAuthors(AuthorQueryParams params) {
        List<AuthorWithInstitutionDto> authors = toAuthors(
                recordRepo.findAllByCriteria(toCriteria(params)),
                StatisticsLabel.SYSTEM_AUTHORS_SCATTER,
                params.getInterval());

        return buildStatistics(authors,
                StatisticsLabel.SYSTEM_AUTHORS_SCATTER,
                StatisticsLabel.SYSTEM_AUTHORS_OVERVIEW,
                params.getInterval(),
                "All authors ranked by activity across the system");
    }

    @Override
    public AuthorsStatisticsDto getInstitutionAuthors(AuthorQueryParams params) {
        List<RMRecord> records = recordFilter.byInstitution(
                recordRepo.findAllByCriteria(toCriteria(params)),
                requireCurrentInstitutionUri());

        List<AuthorWithInstitutionDto> authors = toAuthors(
                records, StatisticsLabel.INSTITUTION_AUTHORS_SCATTER, params.getInterval());

        return buildStatistics(authors,
                StatisticsLabel.INSTITUTION_AUTHORS_SCATTER,
                StatisticsLabel.INSTITUTION_AUTHORS_OVERVIEW,
                params.getInterval(),
                "All authors ranked by activity in institution " + userContext.getInstitutionName());
    }

    @Override
    public AuthorOverviewDto getAuthorByUsername(String username, StatisticsInterval interval) {
        RMUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new StatisticsNotFoundException("Author not found: " + username));

        List<RMRecord> records = recordFilter.byAuthor(
                recordRepo.findAllByInterval(interval), user.getUri());
        AnswerCounts counts = answerStatsRepo.countByAuthor(interval)
                .getOrDefault(user.getUri(), AnswerCounts.empty(user.getUri()));

        return authorBuilder.build(user, records, counts, StatisticsLabel.PERSONAL_OVERVIEW, interval);
    }

    private AuthorsStatisticsDto buildStatistics(
            List<AuthorWithInstitutionDto> authors,
            StatisticsLabel listLabel,
            StatisticsLabel overviewLabel,
            StatisticsInterval interval,
            String description) {

        return AuthorsStatisticsDto.builder()
                .label(listLabel)
                .description(description)
                .interval(interval)
                .authors(authors)
                .overview(overviewBuilder.build(authors, overviewLabel, interval))
                .build();
    }

    private List<AuthorWithInstitutionDto> authorsFromSystem(StatisticsInterval interval) {
        return toAuthors(recordRepo.findAllByInterval(interval),
                StatisticsLabel.SYSTEM_AUTHORS_SCATTER, interval);
    }

    private List<AuthorWithInstitutionDto> authorsFromCurrentInstitution(StatisticsInterval interval) {
        List<RMRecord> records = recordFilter.byInstitution(
                recordRepo.findAllByInterval(interval), requireCurrentInstitutionUri());
        return toAuthors(records, StatisticsLabel.INSTITUTION_AUTHORS_SCATTER, interval);
    }

    private List<AuthorWithInstitutionDto> toAuthors(
            List<RMRecord> records, StatisticsLabel label, StatisticsInterval interval) {

        Map<URI, AnswerCounts> countsByAuthor = answerStatsRepo.countByAuthor(interval);

        return aggregator.groupByAuthor(records).values().stream()
                .map(grouped -> {
                    RMUser author = grouped.getFirst().getAuthor();
                    AnswerCounts counts = countsByAuthor.getOrDefault(
                            author.getUri(), AnswerCounts.empty(author.getUri()));
                    return authorBuilder.build(author, grouped, counts, label, interval);
                })
                .sorted(Comparator.comparingLong(AuthorWithInstitutionDto::getTotalRecords).reversed())
                .toList();
    }

    private URI requireCurrentInstitutionUri() {
        URI uri = userContext.getInstitutionUri();
        if (uri == null) {
            throw new StatisticsNotFoundException("Current user has no institution assigned");
        }
        return uri;
    }

    private RecordQueryCriteria toCriteria(AuthorQueryParams params) {
        return RecordQueryCriteria.builder()
                .interval(params.getInterval())
                .authorUsernameLike(params.getUsernameFilter())
                .build();
    }
}