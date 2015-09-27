package ru.bmstu.tp.android_client.DataBase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


public class DBContentProvider extends ContentProvider {
    static final String LOG_TAG = "DBContentProvider";
    static final String AUTHORITY = "ru.bmstu.tp.android_client.DBContentProvider";

    static final String USER_PATH = "user";
    public static final Uri USER_URI = Uri.parse("content://" + AUTHORITY + "/" + USER_PATH);
    static final String USER_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + USER_PATH;
    static final int URI_USER = 1;

    static final String USING_CONTROLLER_PATH = "using_controller";
    public static final Uri USING_CONTROLLER_URI = Uri.parse("content://" + AUTHORITY + "/" + USING_CONTROLLER_PATH);
    static final String USING_CONTROLLER_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + USING_CONTROLLER_PATH;
    static final String USING_CONTROLLER_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + USING_CONTROLLER_PATH;
    static final int URI_USING_CONTROLLER = 2;
    static final int URI_USING_CONTROLLER_ID = 3;

    static final String CONTROLLER_PATH = "controller";
    public static final Uri CONTROLLER_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTROLLER_PATH);
    static final String CONTROLLER_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + CONTROLLER_PATH;
    static final String CONTROLLER_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + CONTROLLER_PATH;
    static final int URI_CONTROLLER = 4;
    static final int URI_CONTROLLER_ID = 5;

    static final String BUTTON_PATH = "button";
    public static final Uri BUTTON_URI = Uri.parse("content://" + AUTHORITY + "/" + BUTTON_PATH);
    static final String BUTTON_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + BUTTON_PATH;
    static final String BUTTON_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + BUTTON_PATH;
    static final int URI_BUTTON = 6;
    static final int URI_BUTTON_ID = 7;

    static final String HISTORY_PATH = "history";
    public static final Uri HISTORY_URI = Uri.parse("content://" + AUTHORITY + "/" + HISTORY_PATH);
    static final String HISTORY_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + HISTORY_PATH;
    static final String HISTORY_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + HISTORY_PATH;
    static final int URI_HISTORY = 8;
    static final int URI_HISTORY_ID = 9;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, USER_PATH, URI_USER);
        uriMatcher.addURI(AUTHORITY, USING_CONTROLLER_PATH, URI_USING_CONTROLLER);
        uriMatcher.addURI(AUTHORITY, USING_CONTROLLER_PATH + "/#", URI_USING_CONTROLLER_ID);
        uriMatcher.addURI(AUTHORITY, CONTROLLER_PATH, URI_CONTROLLER);
        uriMatcher.addURI(AUTHORITY, CONTROLLER_PATH + "/#", URI_CONTROLLER_ID);
        uriMatcher.addURI(AUTHORITY, BUTTON_PATH, URI_BUTTON);
        uriMatcher.addURI(AUTHORITY, BUTTON_PATH + "/#", URI_BUTTON_ID);
        uriMatcher.addURI(AUTHORITY, HISTORY_PATH, URI_HISTORY);
        uriMatcher.addURI(AUTHORITY, HISTORY_PATH + "/#", URI_HISTORY_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "delete: " + uri.toString());
        dbHelper.getWritableDatabase();
        int cnt;
        String id;
        switch (uriMatcher.match(uri)) {
            case URI_USING_CONTROLLER_ID :
                selection = "_id = " + uri.getLastPathSegment();
            case URI_USING_CONTROLLER :
                cnt = db.delete(DBSchemas.UsingController.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            case URI_CONTROLLER_ID :
                selection = "_id = " + uri.getLastPathSegment();
            case URI_CONTROLLER :
                cnt = db.delete(DBSchemas.Controller.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            case URI_BUTTON_ID :
                selection = "_id = " + uri.getLastPathSegment();
            case URI_BUTTON :
                cnt = db.delete(DBSchemas.Button.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            case URI_HISTORY_ID :
                selection = "_id = " + uri.getLastPathSegment();
            case URI_HISTORY :
                cnt = db.delete(DBSchemas.History.TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            default:
                throw new IllegalArgumentException("Wrong Uri: " + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType: " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_USER :
                return USER_ITEM_TYPE;
            case URI_USING_CONTROLLER :
                return USING_CONTROLLER_TYPE;
            case URI_USING_CONTROLLER_ID :
                return USING_CONTROLLER_ITEM_TYPE;
            case URI_CONTROLLER :
                return CONTROLLER_TYPE;
            case URI_CONTROLLER_ID :
                return CONTROLLER_ITEM_TYPE;
            case URI_BUTTON :
                return BUTTON_TYPE;
            case URI_BUTTON_ID :
                return BUTTON_ITEM_TYPE;
            case URI_HISTORY :
                return HISTORY_TYPE;
            case URI_HISTORY_ID :
                return HISTORY_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert: " + uri.toString());
        dbHelper.getWritableDatabase();
        long rowID;
        Uri resultUri;
        switch (uriMatcher.match(uri)) {
            case URI_USER :
                rowID = db.insert(DBSchemas.User.TABLE_NAME, null, values);
                resultUri = ContentUris.withAppendedId(USER_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            case URI_USING_CONTROLLER_ID:
            case URI_USING_CONTROLLER :
                rowID = db.insert(DBSchemas.UsingController.TABLE_NAME, null, values);
                resultUri = ContentUris.withAppendedId(USING_CONTROLLER_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            case URI_CONTROLLER_ID :
            case URI_CONTROLLER :
                rowID = db.insert(DBSchemas.Controller.TABLE_NAME, null, values);
                resultUri = ContentUris.withAppendedId(CONTROLLER_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            case URI_BUTTON_ID :
            case URI_BUTTON :
                rowID = db.insert(DBSchemas.Button.TABLE_NAME, null, values);
                resultUri = ContentUris.withAppendedId(BUTTON_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            case URI_HISTORY_ID :
            case URI_HISTORY :
                rowID = db.insert(DBSchemas.History.TABLE_NAME, null, values);
                resultUri = ContentUris.withAppendedId(HISTORY_URI, rowID);
                getContext().getContentResolver().notifyChange(resultUri, null);
                return resultUri;
            default:
                throw new IllegalArgumentException("Wrong Uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query: " + uri.toString());
        dbHelper.getWritableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case URI_USER :
                cursor = db.query(DBSchemas.User.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), USER_URI);
                return cursor;
            case URI_USING_CONTROLLER_ID :
            case URI_USING_CONTROLLER :
                cursor = db.query(DBSchemas.UsingController.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), USING_CONTROLLER_URI);
                return cursor;
            case URI_CONTROLLER_ID :
            case URI_CONTROLLER :
                cursor = db.query(DBSchemas.Controller.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), CONTROLLER_URI);
                return cursor;
            case URI_BUTTON_ID :
            case URI_BUTTON :
                cursor = db.query(DBSchemas.Button.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), BUTTON_URI);
                return cursor;
            case URI_HISTORY_ID :
            case URI_HISTORY :
                cursor = db.query(DBSchemas.History.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), HISTORY_URI);
                return cursor;
            default:
                throw new IllegalArgumentException("Wrong Uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "update: " + uri.toString());
        dbHelper.getWritableDatabase();
        int cnt;
        String id;
        switch (uriMatcher.match(uri)) {
            case URI_USER :
                cnt = db.update(DBSchemas.User.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            case URI_USING_CONTROLLER_ID :
                selection = "_id = " + uri.getLastPathSegment();
            case URI_USING_CONTROLLER :
                cnt = db.update(DBSchemas.UsingController.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            case URI_CONTROLLER_ID :
                selection = "_id = " + uri.getLastPathSegment();
            case URI_CONTROLLER :
                cnt = db.update(DBSchemas.Controller.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            case URI_BUTTON_ID :
                selection = "_id = " + uri.getLastPathSegment();
            case URI_BUTTON :
                cnt = db.update(DBSchemas.Button.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            case URI_HISTORY_ID :
                selection = "_id = " + uri.getLastPathSegment();
            case URI_HISTORY :
                cnt = db.update(DBSchemas.History.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            default:
                throw new IllegalArgumentException("Wrong Uri: " + uri);
        }
    }
}
