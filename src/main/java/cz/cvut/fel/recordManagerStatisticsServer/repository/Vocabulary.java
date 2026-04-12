package cz.cvut.fel.recordManagerStatisticsServer.repository;

public final class Vocabulary {

    public static final String USER_NAMESPACE =
            "http://onto.fel.cvut.cz/ontologies/uzivatel/";

    // -------------------------
    // Prefixes & base URIs
    // -------------------------
    public static final String ACTION_SAVE_RECORD_SUCCESS = "SAVE_RECORD_SUCCESS";
    public static final String ACTION_SAVE_RECORD_ERROR = "SAVE_RECORD_ERROR";
    public static final String ACTION_LOAD_RECORDS_ERROR = "LOAD_RECORDS_ERROR";
    public static final String ACTION_SAVE_INSTITUTION_SUCCESS = "SAVE_INSTITUTION_SUCCESS";
    public static final String ACTION_SAVE_INSTITUTION_ERROR = "SAVE_INSTITUTION_ERROR";
    public static final String ACTION_SAVE_USER_SUCCESS = "SAVE_USER_SUCCESS";
    public static final String ACTION_UNAUTH_USER = "UNAUTH_USER";

    // -------------------------
    // Classes
    // -------------------------
    private static final String RM = "http://onto.fel.cvut.cz/ontologies/record-manager/";
    public static final String s_c_record = RM + "record";
    public static final String s_c_institution = RM + "institution";
    public static final String s_c_action_history = RM + "action-history";
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

    // -------------------------
    // Data properties
    // -------------------------

    public static final String s_p_key = RM + "key";
    public static final String s_p_has_form_template = RM + "has-form-template";
    public static final String s_p_action_type = RM + "action_type";
    public static final String s_p_payload = RM + "payload";
    private static final String DOC = "http://onto.fel.cvut.cz/ontologies/documentation/";
    private static final String FORM = "http://onto.fel.cvut.cz/ontologies/form/";
    private static final String DC = "http://purl.org/dc/terms/";
    public static final String s_p_created = DC + "created";
    private static final String RDFS = "http://www.w3.org/2000/01/rdf-schema#";

    // -------------------------
    // Action type values
    // -------------------------
    public static final String s_p_label = RDFS + "label";
    private static final String FOAF = "http://xmlns.com/foaf/0.1/";
    public static final String s_c_Person = FOAF + "Person";
    // User
    public static final String s_p_firstName = FOAF + "firstName";
    public static final String s_p_lastName = FOAF + "lastName";
    public static final String s_p_accountName = FOAF + "accountName";

    private Vocabulary() {
    }

}