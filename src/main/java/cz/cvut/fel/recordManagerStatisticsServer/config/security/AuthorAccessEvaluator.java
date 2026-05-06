package cz.cvut.fel.recordManagerStatisticsServer.config.security;

import cz.cvut.fel.recordManagerStatisticsServer.repository.UserRepository;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.Institution;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;

@Slf4j
@Component("authorAccess")
@RequiredArgsConstructor
public class AuthorAccessEvaluator {

    private final RequestUserContext userContext;
    private final UserRepository userRepository;

    public boolean canRead(String username) {
        if (username == null) return false;

        if (isSelf(username)) return true;
        if (userContext.canReadAllUsers()) return true;
        if (userContext.canReadOrgUsers() && sameInstitutionAs(username)) return true;

        log.debug("Access denied: caller={} target={}", userContext.getUsername(), username);
        return false;
    }

    private boolean isSelf(String username) {
        return username.equals(userContext.getUsername());
    }

    private boolean sameInstitutionAs(String username) {
        URI callerInstitution = userContext.getInstitutionUri();
        if (callerInstitution == null) return false;

        return userRepository.findByUsername(username)
                .map(u -> u.getInstitution())
                .map(Institution::getUri)
                .map(callerInstitution::equals)
                .orElse(false);
    }
}