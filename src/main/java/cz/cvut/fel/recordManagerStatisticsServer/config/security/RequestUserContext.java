package cz.cvut.fel.recordManagerStatisticsServer.config.security;

import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Component
@RequestScope
public class RequestUserContext {

    private final UserContext context;

    public RequestUserContext() {
        this.context = resolve();
    }

    public URI getUserUri() {
        return context.getUserUri();
    }

    public String getFullName() {
        return context.getFullName();
    }

    private UserContext resolve() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        String subject = jwt.getSubject();
        URI userUri = subject.startsWith("http")
                ? URI.create(subject)
                : URI.create(Vocabulary.USER_NAMESPACE + subject);

        UserContext ctx = UserContext.builder()
                .userUri(userUri)
                .fullName(jwt.getClaimAsString("name"))
                .canReadStatistics(hasRole(jwt, SecurityConstants.READ_STATISTICS))
                .build();

        log.debug("Resolved context: user={}", userUri);
        return ctx;
    }

    private boolean hasRole(Jwt jwt, String role) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) return false;
        Collection<?> roles = (Collection<?>) realmAccess.get("roles");
        return roles != null && roles.contains(role);
    }
}