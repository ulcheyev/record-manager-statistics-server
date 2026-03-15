package cz.cvut.fel.recordManagerStatisticsServer.controller;

import cz.cvut.fel.recordManagerStatisticsServer.dto.RecordStatsDto;
import cz.cvut.fel.recordManagerStatisticsServer.service.RecordStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.Api.RECORDS)
@RequiredArgsConstructor
public class RecordStatisticsController {

    private final RecordStatisticsService service;

    @GetMapping(Constants.Api.STATS)
    public ResponseEntity<RecordStatsDto> getRecordStats() {
        return ResponseEntity.ok(service.getRecordStats());
    }
}