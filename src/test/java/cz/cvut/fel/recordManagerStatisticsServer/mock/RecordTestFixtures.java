package cz.cvut.fel.recordManagerStatisticsServer.mock;

import cz.cvut.fel.recordManagerStatisticsServer.model.RMUser;
import cz.cvut.fel.recordManagerStatisticsServer.repository.Vocabulary;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.Institution;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RMRecord;
import cz.cvut.fel.recordManagerStatisticsServer.repository.model.RecordPhase;

import java.net.URI;
import java.util.Date;
import java.util.List;

public final class RecordTestFixtures {

    public static final URI INSTITUTION_URI =
            URI.create(Vocabulary.s_c_institution + "/inst-1");
    public static final URI INSTITUTION_URI_2 =
            URI.create(Vocabulary.s_c_institution + "/inst-2");
    public static final URI AUTHOR_URI =
            URI.create(Vocabulary.USER_NAMESPACE + "/author-1");
    public static final URI AUTHOR_URI_2 =
            URI.create(Vocabulary.USER_NAMESPACE + "/author-2");
    private RecordTestFixtures() {
    }

    public static Institution institution(URI uri, String name) {
        Institution inst = new Institution();
        inst.setUri(uri);
        inst.setName(name);
        return inst;
    }

    public static RMUser author(URI uri, String firstName, String lastName,
                                String accountName, Institution institution) {
        RMUser user = new RMUser();
        user.setUri(uri);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAccountName(accountName);
        user.setInstitution(institution);
        return user;
    }

    public static RMRecord record(URI uri, RecordPhase phase,
                                  RMUser author, Institution institution,
                                  String formTemplate, Date dateCreated) {
        RMRecord record = new RMRecord();
        record.setUri(uri);
        record.setPhase(phase);
        record.setAuthor(author);
        record.setInstitution(institution);
        record.setFormTemplate(formTemplate);
        record.setDateCreated(dateCreated);
        return record;
    }

    public static List<RMRecord> sampleRecords() {
        Institution inst1 = institution(INSTITUTION_URI, "General Hospital");
        Institution inst2 = institution(INSTITUTION_URI_2, "City Clinic");
        RMUser author1 = author(AUTHOR_URI, "John", "Doe", "jdoe", inst1);
        RMUser author2 = author(AUTHOR_URI_2, "Jane", "Smith", "jsmith", inst2);

        return List.of(
                record(URI.create("http://record/1"), RecordPhase.OPEN,
                        author1, inst1, "form-a", new Date(1_700_000_000_000L)),
                record(URI.create("http://record/2"), RecordPhase.COMPLETED,
                        author1, inst1, "form-a", new Date(1_700_100_000_000L)),
                record(URI.create("http://record/3"), RecordPhase.REJECTED,
                        author2, inst2, "form-b", new Date(1_700_200_000_000L)),
                record(URI.create("http://record/4"), RecordPhase.OPEN,
                        author2, inst2, "form-b", new Date(1_700_300_000_000L)),
                record(URI.create("http://record/5"), RecordPhase.COMPLETED,
                        author1, inst1, "form-a", new Date(1_700_400_000_000L))
        );
    }

    public static RMRecord recordWithTemplate(String templateUri) {
        RMRecord record = new RMRecord();
        record.setUri(URI.create("http://record/template-" + templateUri.hashCode()));
        record.setPhase(RecordPhase.OPEN);
        record.setFormTemplate(templateUri);
        return record;
    }
}