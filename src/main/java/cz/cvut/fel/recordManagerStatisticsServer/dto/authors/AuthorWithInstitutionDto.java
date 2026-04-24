package cz.cvut.fel.recordManagerStatisticsServer.dto.authors;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.net.URI;

@Data
@SuperBuilder
public class AuthorWithInstitutionDto extends AuthorOverviewDto {
    private String institutionName;
    private URI institutionUri;
}