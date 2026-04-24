package cz.cvut.fel.recordManagerStatisticsServer.controller;

import cz.cvut.fel.recordManagerStatisticsServer.config.security.SecurityConstants;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorOverviewDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorQueryParams;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorsOverviewDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.authors.AuthorsStatisticsDto;
import cz.cvut.fel.recordManagerStatisticsServer.service.AuthorsStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.Api.AUTHORS)
@RequiredArgsConstructor
@PreAuthorize(SecurityConstants.HAS_STATISTICS)
public class AuthorsStatisticsController {

    private final AuthorsStatisticsService authorsStatisticsService;

    @GetMapping("/all/overview")
    @PreAuthorize(SecurityConstants.HAS_ALL_USERS)
    public ResponseEntity<AuthorsOverviewDto> getAllAuthorsOverview(StatisticsInterval interval) {
        return ResponseEntity.ok(authorsStatisticsService.getAllAuthorsOverview(interval));
    }

    @GetMapping("/all")
    @PreAuthorize(SecurityConstants.HAS_ALL_USERS)
    public ResponseEntity<AuthorsStatisticsDto> getAllAuthors(
            StatisticsInterval interval,
            @RequestParam(required = false) String username) {

        return ResponseEntity.ok(authorsStatisticsService.getAllAuthors(
                AuthorQueryParams.builder()
                        .interval(interval)
                        .usernameFilter(username)
                        .build()));
    }

    @GetMapping("/institution")
    @PreAuthorize(SecurityConstants.HAS_ORG_USERS)
    public ResponseEntity<AuthorsStatisticsDto> getInstitutionAuthors(
            StatisticsInterval interval,
            @RequestParam(required = false) String username) {

        return ResponseEntity.ok(authorsStatisticsService.getInstitutionAuthors(
                AuthorQueryParams.builder()
                        .interval(interval)
                        .usernameFilter(username)
                        .build()));
    }

    @GetMapping("/institution/overview")
    @PreAuthorize(SecurityConstants.HAS_ORG_USERS)
    public ResponseEntity<AuthorsOverviewDto> getInstitutionAuthorsOverview(StatisticsInterval interval) {
        return ResponseEntity.ok(authorsStatisticsService.getInstitutionAuthorsOverview(interval));
    }


    @GetMapping("/{username}")
    @PreAuthorize(SecurityConstants.CAN_READ_AUTHOR)
    public ResponseEntity<AuthorOverviewDto> getAuthorByUsername(
            @PathVariable String username,
            StatisticsInterval interval) {
        return ResponseEntity.ok(authorsStatisticsService.getAuthorByUsername(username, interval));
    }
}