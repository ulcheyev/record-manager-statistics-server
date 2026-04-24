package cz.cvut.fel.recordManagerStatisticsServer.shared;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
        public static final String RECORDS = "/records";
        public static final String AUTHORS = "/authors";
        public static final String INSTITUTIONS = "/institutions";
        public static final String PERMISSIONS = "/permissions";

        private Api() {
        }
    }


    public static final class DateFormats {

        public static final DateTimeFormatter DATE =
                DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);

        public static final DateTimeFormatter DATE_TIME =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC);

        private DateFormats() {
        }
    }
}