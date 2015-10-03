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
                    User.RATING + " INTEGER," +
                    User.GAMES_COUNT + " INTEGER," +
                    ")";

    public static final String SQL_DELETE_USER =
            "DROP TABLE IF EXISTS " + User.TABLE_NAME;

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "user";

        public static final String NAME = "name";
        public static final String PASSWORD = "password"; // only hash of password
        public static final String USER_ID = "user_id";
        public static final String RATING = "rating";
        public static final String GAMES_COUNT = "games_count";
    }

    // ------------- friend -----------------

    public static final String SQL_CREATE_TABLE_FRIEND =
            "CREATE TABLE " + UsingController.TABLE_NAME + " (" +
                    UsingController._ID + " INTEGER PRIMARY KEY," +
                    UsingController.NAME + " VARCHAR(30)," +
                    UsingController.STATUS + " INTEGER," +
                    UsingController.RATING + " INTEGER," +
                    ")";

    public static final String SQL_DELETE_FREIND =
            "DROP TABLE IF EXISTS " + UsingController.TABLE_NAME;

    public static abstract class UsingController implements BaseColumns {
        public static final String TABLE_NAME = "friend";

        public static final String NAME = "name";
        public static final String STATUS = "status";
        public static final String RATING = "rating";
    }
}
