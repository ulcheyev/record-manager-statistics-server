package cz.cvut.fel.recordManagerStatisticsServer.config.security;

public final class SecurityConstants {

    public static final String READ_STATISTICS = "read-statistics-role";
    public static final String HAS_STATISTICS =
            "hasRole('" + READ_STATISTICS + "')";

    private SecurityConstants() {
    }
}