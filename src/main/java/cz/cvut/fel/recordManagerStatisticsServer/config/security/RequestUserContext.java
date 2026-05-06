package cz.cvut.fel.recordManagerStatisticsServer.config.security;

import cz.cvut.fel.recordManagerStatisticsServer.repository.UserRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.Institution;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequestScope
public class RequestUserContext {

    private final UserContext context;
    private final UserRepository userRepository;

    @Autowired
    public RequestUserContext(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.context = resolve();
    }


    public boolean canReadAllRecords() {
        return context.isCanReadAllRecords();
    }

    public boolean canReadOrgRecords() {
        return context.isCanReadOrgRecords();
    }

    public boolean canReadAllUsers() {
        return context.isCanReadAllUsers();
    }

    public boolean canReadOrgUsers() {
        return context.isCanReadOrgUsers();
    }

    public boolean canReadAllOrganizations() {
        return context.isCanReadAllOrganizations();
    }

    public boolean canReadOrganization() {
        return context.isCanReadOrganization();
    }

    public boolean canReadStatistics() {
        return context.isCanReadStatistics();
    }

    public URI getUserUri() {
        return context.getUserUri();
    }

    public String getUsername() {
        return context.getUsername();
    }

    public String getFullName() {
        return context.getFullName();
    }

    public URI getInstitutionUri() {
        return context.getInstitutionUri();
    }

    public String getInstitutionName() {
        if (Objects.isNull(context.getInstitutionName())) {
            log.warn("User {} has no institution name in context", getUserUri());
            return "";

        }
        return context.getInstitutionName();
    }

    private UserContext resolve() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String subject = jwt.getSubject();
        URI userUri = subject.startsWith("http")
                ? URI.create(subject)
                : URI.create(Vocabulary.USER_NAMESPACE + subject);

        Optional<RMUser> user = userRepository.findByUri(userUri);

        UserContext ctx = UserContext.builder()
                .userUri(userUri)
                .username(user.map(RMUser::getAccountName)
                        .orElseGet(() -> jwt.getClaimAsString("preferred_username")))
                .fullName(jwt.getClaimAsString("name"))
                .institutionUri(user.map(RMUser::getInstitution)
                        .map(Institution::getUri)
                        .orElse(null))
                .institutionName(user.map(RMUser::getInstitution)
                        .map(Institution::getName)
                        .orElse(null))
                .canReadAllRecords(hasRole(jwt, SecurityConstants.ROLE_READ_ALL_RECORDS))
                .canReadOrgRecords(hasRole(jwt, SecurityConstants.ROLE_READ_ORG_RECORDS))
                .canReadAllUsers(hasRole(jwt, SecurityConstants.ROLE_READ_ALL_USERS))
                .canReadOrgUsers(hasRole(jwt, SecurityConstants.ROLE_READ_ORG_USERS))
                .canReadAllOrganizations(hasRole(jwt, SecurityConstants.ROLE_READ_ALL_ORGS))
                .canReadOrganization(hasRole(jwt, SecurityConstants.ROLE_READ_ORG))
                .canReadStatistics(hasRole(jwt, SecurityConstants.ROLE_READ_STATISTICS))
                .build();

        log.debug("Resolved context: user={} username={} institution={}",
                userUri, ctx.getUsername(), ctx.getInstitutionName());
        return ctx;
    }

    private boolean hasRole(Jwt jwt, String role) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) return false;
        Collection<?> roles = (Collection<?>) realmAccess.get("roles");
        return roles != null && roles.contains(role);
    }
}