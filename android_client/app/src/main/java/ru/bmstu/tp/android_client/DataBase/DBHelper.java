package ru.bmstu.tp.android_client.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBSchemas.DATABASE_NAME, null, DBSchemas.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSchemas.SQL_CREATE_TABLE_USER);
        db.execSQL(DBSchemas.SQL_CREATE_TABLE_FRIEND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBSchemas.SQL_DELETE_USER);
        db.execSQL(DBSchemas.SQL_DELETE_FREIND);
        onCreate(db);
    }
}
