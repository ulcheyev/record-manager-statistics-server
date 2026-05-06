package cz.cvut.fel.recordManagerStatisticsServer.config.security;

public final class SecurityConstants {

    public static final String ROLE_READ_ALL_RECORDS = "read-all-records-role";
    public static final String ROLE_READ_ORG_RECORDS = "read-organization-records-role";
    public static final String ROLE_READ_ALL_USERS = "read-all-users-role";
    public static final String ROLE_READ_ORG_USERS = "read-organization-users-role";
    public static final String ROLE_READ_ALL_ORGS = "read-all-organizations-role";
    public static final String ROLE_READ_ORG = "read-organization-role";
    public static final String ROLE_READ_STATISTICS = "read-statistics-role";
    public static final String HAS_ALL_RECORDS = "hasRole('ROLE_" + ROLE_READ_ALL_RECORDS + "')";
    public static final String HAS_ORG_RECORDS = "hasRole('ROLE_" + ROLE_READ_ORG_RECORDS + "')";
    public static final String HAS_ALL_USERS = "hasRole('ROLE_" + ROLE_READ_ALL_USERS + "')";
    public static final String HAS_ORG_USERS = "hasRole('ROLE_" + ROLE_READ_ORG_USERS + "')";
    public static final String HAS_ALL_ORGS = "hasRole('ROLE_" + ROLE_READ_ALL_ORGS + "')";
    public static final String HAS_STATISTICS = "hasRole('ROLE_" + ROLE_READ_STATISTICS + "')";
    public static final String CAN_READ_AUTHOR = "@authorAccess.canRead(#username)";

    private SecurityConstants() {
    }
}