package cz.cvut.fel.recordManagerStatisticsServer.service.impl.records;

import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsInterval;
import cz.cvut.fel.recordManagerStatisticsServer.dto.StatisticsLabel;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.FormTemplateUsageDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.PhaseDistributionDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordListDto;
import cz.cvut.fel.recordManagerStatisticsServer.dto.records.RecordSummaryDto;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Component
public class RecordListBuilder {

    public RecordListDto build(
            List<RMRecord> sourceRecords,
            List<RecordSummaryDto> summaries,
            Map<URI, String> templateLabels,
            StatisticsLabel label,
            StatisticsInterval interval
    ) {
        return RecordListDto.builder()
                .label(label)
                .description(summaries.size() + " records created in the selected period. See details by clicking on specific record.")
                .interval(interval)
                .total(summaries.size())
                .records(summaries)
                .phaseDistribution(phaseDistribution(sourceRecords, interval))
                .formTemplateUsage(formTemplateUsage(sourceRecords, templateLabels, interval))
                .build();
    }

    private PhaseDistributionDto phaseDistribution(
            List<RMRecord> records,
            StatisticsInterval interval
    ) {
        Map<RecordPhase, Long> counts = records.stream()
                .filter(record -> record.getPhase() != null)
                .collect(groupingBy(RMRecord::getPhase, counting()));

        long total = records.size();

        List<PhaseDistributionDto.PhaseSliceDto> distribution = Arrays.stream(RecordPhase.values())
                .map(phase -> {
                    long count = counts.getOrDefault(phase, 0L);

                    return PhaseDistributionDto.PhaseSliceDto.builder()
                            .phase(phase)
                            .count(count)
                            .percentage(percentage(count, total))
                            .build();
                })
                .toList();

        return PhaseDistributionDto.builder()
                .interval(interval)
                .total(total)
                .distribution(distribution)
                .build();
    }

    private String resolveTemplateLabel(String templateUri, Map<URI, String> templateLabels) {
        if (templateUri == null || templateUri.isBlank()) {
            return "Unknown template";
        }

        try {
            URI uri = URI.create(templateUri);
            return templateLabels.getOrDefault(uri, templateUri);
        } catch (IllegalArgumentException ex) {
            return templateUri;
        }
    }

    private FormTemplateUsageDto formTemplateUsage(
            List<RMRecord> records,
            Map<URI, String> templateLabels,
            StatisticsInterval interval
    ) {
        Map<String, Long> counts = records.stream()
                .filter(record -> record.getFormTemplate() != null)
                .collect(groupingBy(RMRecord::getFormTemplate, counting()));

        long total = records.size();

        List<FormTemplateUsageDto.TemplateSliceDto> templates = counts.entrySet().stream()
                .map(entry -> {
                    String templateUri = entry.getKey();

                    return FormTemplateUsageDto.TemplateSliceDto.builder()
                            .templateUri(templateUri)
                            .templateLabel(resolveTemplateLabel(templateUri, templateLabels))
                            .count(entry.getValue())
                            .percentage(percentage(entry.getValue(), total))
                            .build();
                })
                .sorted(Comparator.comparingLong(FormTemplateUsageDto.TemplateSliceDto::getCount).reversed())
                .toList();

        return FormTemplateUsageDto.builder()
                .interval(interval)
                .total(total)
                .templates(templates)
                .build();
    }

    private double percentage(long value, long total) {
        return total == 0 ? 0.0 : (double) value / total * 100.0;
    }
}