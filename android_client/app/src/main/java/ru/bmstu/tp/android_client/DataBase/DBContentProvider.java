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

    static final String FRIEND_PATH = "friend";
    public static final Uri FRIEND_URI = Uri.parse("content://" + AUTHORITY + "/" + FRIEND_PATH);
    static final String FRIEND_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + FRIEND_PATH;
    static final String FRIEND_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + FRIEND_PATH;
    static final int URI_FRIEND = 2;
    static final int URI_FRIEND_ID = 3;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, USER_PATH, URI_USER);
        uriMatcher.addURI(AUTHORITY, FRIEND_PATH, URI_FRIEND);
        uriMatcher.addURI(AUTHORITY, FRIEND_PATH + "/#", URI_FRIEND_ID);
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
        switch (uriMatcher.match(uri)) {
            case URI_FRIEND_ID :
                selection = "_id = " + uri.getLastPathSegment();
            case URI_FRIEND :
                cnt = db.delete(DBSchemas.UsingController.TABLE_NAME, selection, selectionArgs);
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
            case URI_FRIEND :
                return FRIEND_TYPE;
            case URI_FRIEND_ID :
                return FRIEND_ITEM_TYPE;
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
            case URI_FRIEND_ID:
            case URI_FRIEND :
                rowID = db.insert(DBSchemas.UsingController.TABLE_NAME, null, values);
                resultUri = ContentUris.withAppendedId(FRIEND_URI, rowID);
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
            case URI_FRIEND_ID :
            case URI_FRIEND :
                cursor = db.query(DBSchemas.UsingController.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), FRIEND_URI);
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
            case URI_FRIEND_ID :
                selection = "_id = " + uri.getLastPathSegment();
            case URI_FRIEND :
                cnt = db.update(DBSchemas.UsingController.TABLE_NAME, values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return cnt;
            default:
                throw new IllegalArgumentException("Wrong Uri: " + uri);
        }
    }
}
