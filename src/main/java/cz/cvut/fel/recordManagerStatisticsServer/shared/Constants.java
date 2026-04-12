package cz.cvut.fel.recordManagerStatisticsServer.shared;

public final class Constants {

    private Constants() {
    }

    public static final class Profile {
        public static final String DEMO = "demo";
        public static final String PROD = "prod";

        private Profile() {
        }
    }

    public static final class Api {
        public static final String V1 = "/api/v1";
        public static final String RECORDS = "/records";

        private Api() {
        }
    }
}