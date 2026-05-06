package cz.cvut.fel.recordManagerStatisticsServer.repository.model;

import java.net.URI;


public record CorrectAnswer(URI questionOrigin, URI correctUri, String correctLabel) {

    public String safeLabel() {
        return correctLabel == null ? "" : correctLabel;
    }

}