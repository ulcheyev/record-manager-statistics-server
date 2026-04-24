package cz.cvut.fel.recordManagerStatisticsServer.repository;

public final class Vocabulary {

    // -------------------------
    // Prefixes
    // -------------------------
    public static final String USER_NAMESPACE =
            "http://onto.fel.cvut.cz/ontologies/uzivatel/";
    private static final String FOAF = "http://xmlns.com/foaf/0.1/";
    // User
    public static final String s_c_Person = FOAF + "Person";
    public static final String s_p_firstName = FOAF + "firstName";
    public static final String s_p_lastName = FOAF + "lastName";
    public static final String s_p_accountName = FOAF + "accountName";
    private static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    public static final String s_p_label = RDFS + "label";
    private static final String DC = "http://purl.org/dc/terms/";
    public static final String s_p_created = DC + "created";
    private static final String RM = "http://onto.fel.cvut.cz/ontologies/record-manager/";
    // -------------------------
    // Classes
    // -------------------------
    public static final String s_c_record = RM + "record";
    public static final String s_c_institution = RM + "institution";
    // Record phases
    public static final String s_c_open_record_phase = RM + "open-record-phase";
    public static final String s_c_completed_record_phase = RM + "completed-record-phase";
    public static final String s_c_rejected_record_phase = RM + "rejected-record-phase";
    // -------------------------
    // Object properties
    // -------------------------
    public static final String s_p_has_author = RM + "has-author";
    public static final String s_p_was_treated_at = RM + "was-treated-at";
    public static final String s_p_is_member_of = RM + "is-member-of";
    public static final String s_p_has_phase = RM + "has-phase";
    public static final String s_p_has_question = RM + "has-question";
    // -------------------------
    // Data properties
    // -------------------------
    public static final String s_p_key = RM + "key";
    public static final String s_p_has_form_template = RM + "has-form-template";
    private static final String DOC = "http://onto.fel.cvut.cz/ontologies/documentation/";
    public static final String s_p_has_related_question = DOC + "has_related_question";
    public static final String s_p_has_answer = DOC + "has_answer";
    public static final String s_p_has_object_value = DOC + "has_object_value";
    public static final String s_p_has_data_value = DOC + "has_data_value";
    private static final String FORM = "http://onto.fel.cvut.cz/ontologies/form/";
    public static final String s_p_has_question_origin = FORM + "has-question-origin";
    public static final String s_p_has_correct_answer = FORM + "has-correct-answer-value";

    private Vocabulary() {
    }
}