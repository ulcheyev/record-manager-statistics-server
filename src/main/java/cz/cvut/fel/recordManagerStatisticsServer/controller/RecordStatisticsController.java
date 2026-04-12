package cz.cvut.fel.recordManagerStatisticsServer.controller;

import cz.cvut.fel.recordManagerStatisticsServer.config.security.SecurityConstants;
import cz.cvut.fel.recordManagerStatisticsServer.dto.Granularity;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.record.*;
import cz.cvut.fel.recordManagerStatisticsServer.service.RecordStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.Api.RECORDS)
@RequiredArgsConstructor
public class RecordStatisticsController {

    private final RecordStatisticsService service;

    @GetMapping("/general")
    @PreAuthorize(SecurityConstants.HAS_STATISTICS)
    public ResponseEntity<GeneralRecordStatsDto> getGeneral(StatisticsInterval interval) {
        return ResponseEntity.ok(service.getGeneral(interval));
    }

    @GetMapping("/timeline")
    @PreAuthorize(SecurityConstants.HAS_STATISTICS)
    public ResponseEntity<RecordTimelineDto> getTimeline(
            StatisticsInterval interval,
            @RequestParam(defaultValue = "MONTH") Granularity granularity) {
        return ResponseEntity.ok(service.getTimeline(interval, granularity));
    }

    @GetMapping("/phase-distribution")
    @PreAuthorize(SecurityConstants.HAS_STATISTICS)
    public ResponseEntity<PhaseDistributionDto> getPhaseDistribution(StatisticsInterval interval) {
        return ResponseEntity.ok(service.getPhaseDistribution(interval));
    }

    @GetMapping("/by-institution")
    @PreAuthorize(SecurityConstants.HAS_STATISTICS)
    public ResponseEntity<InstitutionStatsDto> getByInstitution(StatisticsInterval interval) {
        return ResponseEntity.ok(service.getByInstitution(interval));
    }

    @GetMapping("/by-author")
    @PreAuthorize(SecurityConstants.HAS_STATISTICS)
    public ResponseEntity<AuthorStatsDto> getByAuthor(StatisticsInterval interval) {
        return ResponseEntity.ok(service.getByAuthor(interval));
    }

    @GetMapping("/by-form-template")
    @PreAuthorize(SecurityConstants.HAS_STATISTICS)
    public ResponseEntity<FormTemplateUsageDto> getByFormTemplate(StatisticsInterval interval) {
        return ResponseEntity.ok(service.getByFormTemplate(interval));
    }
}