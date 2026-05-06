package cz.cvut.fel.recordManagerStatisticsServer.controller;

import cz.cvut.fel.recordManagerStatisticsServer.config.security.SecurityConstants;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordListDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordsQueryParams;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import cz.cvut.fel.recordManagerStatisticsServer.service.RecordsStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.Api.RECORDS)
@RequiredArgsConstructor
@PreAuthorize(SecurityConstants.HAS_STATISTICS)
public class RecordsStatisticsController {

    private final RecordsStatisticsService recordsStatisticsService;

    @GetMapping
    public ResponseEntity<RecordListDto> getPersonalRecords(
            StatisticsInterval interval,
            @RequestParam(required = false) String      formTemplate,
            @RequestParam(required = false) RecordPhase phase) {

        return ResponseEntity.ok(recordsStatisticsService.getPersonalRecords(
                toParams(interval, formTemplate, phase)));
    }

    @GetMapping("/institution")
    @PreAuthorize(SecurityConstants.HAS_ORG_RECORDS)
    public ResponseEntity<RecordListDto> getInstitutionRecords(
            StatisticsInterval interval,
            @RequestParam(required = false) String      formTemplate,
            @RequestParam(required = false) RecordPhase phase) {

        return ResponseEntity.ok(recordsStatisticsService.getInstitutionRecords(
                toParams(interval, formTemplate, phase)));
    }

    @GetMapping("/all")
    @PreAuthorize(SecurityConstants.HAS_ALL_RECORDS)
    public ResponseEntity<RecordListDto> getSystemRecords(
            StatisticsInterval interval,
            @RequestParam(required = false) String      formTemplate,
            @RequestParam(required = false) RecordPhase phase) {

        return ResponseEntity.ok(recordsStatisticsService.getSystemRecords(
                toParams(interval, formTemplate, phase)));
    }

    private RecordsQueryParams toParams(
            StatisticsInterval interval,
            String formTemplate,
            RecordPhase phase) {

        return RecordsQueryParams.builder()
                .interval(interval)
                .formTemplateFilter(formTemplate)
                .phaseFilter(phase)
                .build();
    }
}