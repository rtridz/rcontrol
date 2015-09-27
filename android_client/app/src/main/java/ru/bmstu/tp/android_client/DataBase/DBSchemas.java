package ru.bmstu.tp.android_client.DataBase;

import android.provider.BaseColumns;

public final class DBSchemas {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Rcontrol";

    public DBSchemas() {}

    // ------------- user -----------------

    public static final String SQL_CREATE_TABLE_USER =
            "CREATE TABLE " + User.TABLE_NAME + " (" +
                    User._ID + " INTEGER PRIMARY KEY," +
                    User.NAME + " VARCHAR(30)," +
                    User.PASSWORD + " VARCHAR(64)," +
                    User.USER_ID + " INTEGER," +
                    ")";

    public static final String SQL_DELETE_USER =
            "DROP TABLE IF EXISTS " + User.TABLE_NAME;

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "user";

        public static final String NAME = "name";
        public static final String PASSWORD = "password"; // only hash of password
        public static final String USER_ID = "user_id";
    }

    // ------------- using_controller -----------------

    public static final String SQL_CREATE_TABLE_USING_CONTROLLER =
            "CREATE TABLE " + UsingController.TABLE_NAME + " (" +
                    UsingController._ID + " INTEGER PRIMARY KEY," +
                    UsingController.NAME + " VARCHAR(30)," +
                    UsingController.CONTROLLER_ID + " INTEGER," +
                    ")";

    public static final String SQL_DELETE_USING_CONTROLLER =
            "DROP TABLE IF EXISTS " + UsingController.TABLE_NAME;

    public static abstract class UsingController implements BaseColumns {
        public static final String TABLE_NAME = "using_controller";

        public static final String NAME = "name";
        public static final String CONTROLLER_ID = "controller_id";
    }

    // ------------- controller -----------------

    public static final String SQL_CREATE_TABLE_CONTROLLER =
            "CREATE TABLE " + Controller.TABLE_NAME + " (" +
                    Controller._ID + " INTEGER PRIMARY KEY," +
                    Controller.TYPE + " VARCHAR(30)," +
                    Controller.TYPE_VERSION + " INTEGER," +
                    Controller.TYPE + " VARCHAR(40)," +
                    Controller.TYPE + " VARCHAR(100)," +
                    ")";

    public static final String SQL_DELETE_CONTROLLER =
            "DROP TABLE IF EXISTS " + Controller.TABLE_NAME;

    public static abstract class Controller implements BaseColumns {
        public static final String TABLE_NAME = "controller";

        public static final String TYPE = "type"; // TV, conditioning, Audio/video player (HI-FI) and etc.
        public static final String TYPE_VERSION = "type_version"; // version of template (pult) of type
        public static final String BRAND = "brand"; // brand of pult
        public static final String BRAND_NAME = "brand"; // full name of pult
    }

    // ------------- button -----------------

    public static final String SQL_CREATE_TABLE_BUTTON =
            "CREATE TABLE " + Button.TABLE_NAME + " (" +
                    Button._ID + " INTEGER PRIMARY KEY," +
                    Button.TYPE + " INTEGER," +
                    Button.SIGNAL + " BINARY," +
                    Button.CONTROLLER_ID + " INTEGER," +
                    ")";

    public static final String SQL_DELETE_BUTTON =
            "DROP TABLE IF EXISTS " + Button.TABLE_NAME;

    public static abstract class Button implements BaseColumns {
        public static final String TABLE_NAME = "button";

        public static final String TYPE = "type";
        public static final String SIGNAL = "signal";
        public static final String CONTROLLER_ID = "controller_id";
    }

    // ------------- history -----------------

    public static final String SQL_CREATE_TABLE_HISTORY =
            "CREATE TABLE " + History.TABLE_NAME + " (" +
                    History._ID + " INTEGER PRIMARY KEY," +
                    History.BUTTON_TYPE + " INTEGER," +
                    History.CONTROLLER_ID + " INTEGER," +
                    ")";

    public static final String SQL_DELETE_HISTORY =
            "DROP TABLE IF EXISTS " + History.TABLE_NAME;

    public static abstract class History implements BaseColumns {
        public static final String TABLE_NAME = "history";

        public static final String BUTTON_TYPE = "button_type";
        public static final String CONTROLLER_ID = "controller_id";
    }
}
