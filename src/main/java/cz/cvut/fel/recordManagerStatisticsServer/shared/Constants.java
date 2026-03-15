package cz.cvut.fel.recordManagerStatisticsServer.shared;

public final class Constants {

    private Constants() {}

    public static final class Profile {
        private Profile() {}

        public static final String DEMO = "demo";
        public static final String PROD = "prod";
    }

    public static final class Api {
        private Api() {}

        public static final String V1      = "/api/v1";
        public static final String RECORDS = "/records";
    }
}