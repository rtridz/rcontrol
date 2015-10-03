package ru.bmstu.tp.android_client.Services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import ru.bmstu.tp.android_client.DataBase.DBContentProvider;
import ru.bmstu.tp.android_client.DataBase.DBSchemas;
import ru.bmstu.tp.android_client.Services.Exceptions.BadRequestException;
import ru.bmstu.tp.android_client.Services.Exceptions.BadUserIdException;
import ru.bmstu.tp.android_client.Services.Exceptions.ForbiddenException;
import ru.bmstu.tp.android_client.Services.Exceptions.InitialServerException;
import ru.bmstu.tp.android_client.Services.Exceptions.NonAuthoritativeException;
import ru.bmstu.tp.android_client.Services.Exceptions.NotFoundException;
import ru.bmstu.tp.android_client.Services.Exceptions.ServerConnectException;
import ru.bmstu.tp.android_client.Services.SQLReader.NmapScanResult;

public class APIService extends IntentService {
    public enum Action {
        CHECK_SERVER,
        REGISTR, AUTH, GCM_ID,
        FRIEND_SEARCH, FRIEND_ADD, FRIEND_LIST, FRIEND_LIST_ONLINE, FRIEND_INVITE,
        GAME_CREATE
    }
    private APISender sender;

    public APIService() {
        super("APIService");
        Cursor cursor = getContentResolver().query(DBContentProvider.USER_URI,
                new String[]{DBSchemas.User.USER_ID}, null, null, null);
        if (cursor.moveToFirst())
            sender = new APISender(cursor.getInt(0));
        else
            sender = new APISender(0);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch (Action.valueOf(intent.getAction())) {
            case CHECK_SERVER :
                checkServerConnect(intent);
                break;
            case REGISTR :
                registration(intent);
                break;
            case AUTH :
                authorization(intent);
                break;
            case FRIEND_SEARCH :
                break;
            case FRIEND_ADD :
                break;
            case FRIEND_LIST :
                break;
            case FRIEND_LIST_ONLINE :
                break;
            case FRIEND_INVITE :
                break;
            case GAME_CREATE :
                break;
            case GCM_ID :
                sendGcmId(intent);
                break;
        }
    }

    private void registration(Intent intent) {
        try {
             sender.registrate(intent.getStringExtra("name"), intent.getStringExtra("password"));
        } catch (ServerConnectException | InitialServerException e) {
            e.printStackTrace();
            return;
        } catch (BadRequestException e) {
            e.printStackTrace();
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(DBSchemas.User.NAME, intent.getStringExtra("name"));
        cv.put(DBSchemas.User.PASSWORD, intent.getStringExtra("password"));
        cv.put(DBSchemas.User.USER_ID, sender.getUserId());
        cv.put(DBSchemas.User.RATING, 0);
        cv.put(DBSchemas.User.GAMES_COUNT, 0);
        getContentResolver().insert(DBContentProvider.USER_URI, cv);
    }

    private void authorization(Intent intent) {
        try {
            sender.registrate(intent.getStringExtra("name"), intent.getStringExtra("password"));
        } catch (ServerConnectException | InitialServerException e) {
            e.printStackTrace();
            return;
        } catch (BadRequestException e) {
            e.printStackTrace();
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(DBSchemas.User.NAME, intent.getStringExtra("name"));
        cv.put(DBSchemas.User.PASSWORD, intent.getStringExtra("password"));
        cv.put(DBSchemas.User.USER_ID, sender.getUserId());
        cv.put(DBSchemas.User.RATING, 0);
        cv.put(DBSchemas.User.GAMES_COUNT, 0);
        Cursor cursor = getContentResolver().query(DBContentProvider.USER_URI, new String[]{"_id"}, null, null, null);
        if (cursor.moveToFirst()) {
            getContentResolver().update(DBContentProvider.USER_URI, cv, DBSchemas.User.USER_ID + " = ?", new String[]{cursor.getString(0)});
        } else {
            getContentResolver().insert(DBContentProvider.USER_URI, cv);
        }
    }

    private void sendGcmId(Intent intent) {
        try {
            sender.sendGcmId(intent.getStringExtra("gcm_id"));
        } catch (ServerConnectException | InitialServerException e) {
            e.printStackTrace();
        }
    }

    private void checkServerConnect(Intent intent) {
        try {
            sender.checkConnect();
        } catch (ServerConnectException | InitialServerException e) {
            e.printStackTrace();
        }
    }
}
