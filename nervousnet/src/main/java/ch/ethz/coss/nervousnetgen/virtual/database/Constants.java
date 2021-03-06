package ch.ethz.coss.nervousnetgen.virtual.database;

/**
 * Created by ales on 07/10/16.
 */
public class Constants {

    public static final String DATABASE_NAME = "VirtualSensorDB";
    public static final int DATABASE_VERSION = 1;

    public static final String POSSIBLE_STATES_TABLE = "PossibleStates";
    public static final String ID = "ID";

    public static final String POSSIBLE_STATES_ID = "PSID";

    public static final String TIMESTAMP = "timestamp";

    public static final int FIELD_TYPE_NULL = 0;
    public static final int FIELD_TYPE_INTEGER = 1;
    public static final int FIELD_TYPE_FLOAT = 2;
    public static final int FIELD_TYPE_STRING = 3;
    public static final int FIELD_TYPE_BLOB = 4;
}
