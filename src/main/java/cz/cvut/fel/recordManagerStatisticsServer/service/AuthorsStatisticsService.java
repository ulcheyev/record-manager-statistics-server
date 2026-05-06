package cz.cvut.fel.recordManagerStatisticsServer.service;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorOverviewDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorQueryParams;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorsOverviewDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorsStatisticsDto;

public interface AuthorsStatisticsService {

    AuthorsOverviewDto getAllAuthorsOverview(StatisticsInterval interval);

    AuthorsOverviewDto getInstitutionAuthorsOverview(StatisticsInterval interval);

    AuthorOverviewDto getAuthorByUsername(String username, StatisticsInterval interval);

    AuthorsStatisticsDto getAllAuthors(AuthorQueryParams params);

    AuthorsStatisticsDto getInstitutionAuthors(AuthorQueryParams params);
}