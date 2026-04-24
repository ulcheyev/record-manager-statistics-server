package cz.cvut.fel.recordManagerStatisticsServer.service.impl.institutions;

import cz.cvut.fel.recordManagerStatisticsServer.dto.AnswerCounts;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsLabel;
import cz.cvut.fel.recordManagerStatisticsServer.dto.institutions.InstitutionSummaryDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.institutions.InstitutionsStatisticsDto;
import cz.cvut.fel.recordManagerStatisticsServer.repository.AnswerStatsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.RecordStatisticsRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.Institution;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.service.InstitutionsStatisticsService;
import cz.cvut.fel.recordManagerStatisticsServer.shared.utils.RecordAggregator;
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
public class DefaultInstitutionsStatisticsService implements InstitutionsStatisticsService {

    private final RecordStatisticsRepository recordRepo;
    private final AnswerStatsRepository answerStatsRepo;
    private final RecordAggregator aggregator;
    private final InstitutionSummaryBuilder summaryBuilder;
    private final InstitutionsOverviewBuilder overviewBuilder;

    @Override
    public InstitutionsStatisticsDto getAllInstitutions(StatisticsInterval interval) {
        List<InstitutionSummaryDto> institutions = buildSummaries(interval);

        InstitutionsStatisticsDto.InstitutionsStatisticsDtoBuilder<?, ?> builder =
                InstitutionsStatisticsDto.builder()
                        .label(StatisticsLabel.SYSTEM_INSTITUTIONS_OVERVIEW)
                        .description(institutions.size() + " institutions contributed records")
                        .interval(interval)
                        .totalInstitutions(institutions.size())
                        .institutions(institutions);

        overviewBuilder.applyLeaders(builder, institutions);
        return builder.build();
    }

    private List<InstitutionSummaryDto> buildSummaries(StatisticsInterval interval) {
        List<RMRecord> records = recordRepo.findAllByInterval(interval);
        Map<URI, AnswerCounts> countsByInstitution = answerStatsRepo.countByInstitution(interval);

        return aggregator.groupByInstitution(records).values().stream()
                .map(group -> toSummary(group, countsByInstitution))
                .sorted(Comparator.comparingLong(InstitutionSummaryDto::getTotalRecords).reversed())
                .toList();
    }

    private InstitutionSummaryDto toSummary(
            List<RMRecord> institutionRecords,
            Map<URI, AnswerCounts> countsByInstitution) {

        Institution institution = institutionRecords.getFirst().getInstitution();
        AnswerCounts counts = countsByInstitution.getOrDefault(
                institution.getUri(), AnswerCounts.empty(institution.getUri()));

        return summaryBuilder.build(institution, institutionRecords, counts);
    }
}