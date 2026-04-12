package cz.cvut.fel.recordManagerStatisticsServer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhaseCountDto {

    private long open;
    private long completed;
    private long rejected;

    public long total() {
        return open + completed + rejected;
    }

    public double completionRate() {
        long t = total();
        return t == 0 ? 0.0 : (double) completed / t * 100;
    }

    public double rejectionRate() {
        long t = total();
        return t == 0 ? 0.0 : (double) rejected / t * 100;
    }
}