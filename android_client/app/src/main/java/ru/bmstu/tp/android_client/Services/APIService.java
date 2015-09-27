package ru.bmstu.tp.android_client.Services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import ru.bmstu.tp.android_client.DataBase.DBContentProvider;
import ru.bmstu.tp.android_client.DataBase.DBSchemas;
import ru.bmstu.tp.android_client.Services.Exceptions.BadRequestException;
import ru.bmstu.tp.android_client.Services.Exceptions.BadSessionIdException;
import ru.bmstu.tp.android_client.Services.Exceptions.InitialServerException;
import ru.bmstu.tp.android_client.Services.Exceptions.ServerConnectException;
import ru.bmstu.tp.android_client.Services.SQLReader.NmapScanResult;

public class APIService extends IntentService {
    public enum Action {
        REGISTR, CHECK_SERVER, SEND_REQUEST
    }
    private APISender sender;

    public APIService() {
        super("APIService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch (Action.valueOf(intent.getAction())) {
            case REGISTR :
                registrateApp(intent);
                break;
            case CHECK_SERVER :
                checkServerConnect(intent);
                break;
            case SEND_REQUEST :
                sendAPIRequest(intent);
                break;
        }
    }

    private void registrateApp(Intent intent) {
        if (sender == null) {
            sender = new APISender(0);
        }
        try {
             sender.updateSessionId();
        } catch (ServerConnectException | InitialServerException e) {
            e.printStackTrace();
        }
        ContentValues cv = new ContentValues();
        cv.put(DBSchemas.User.USER_ID, sender.getUserId());
        Cursor cursor = getContentResolver().query(DBContentProvider.USER_URI, new String[]{"_id"}, null, null, null);
        if (cursor.moveToFirst()) {
            getContentResolver().update(DBContentProvider.USER_URI, cv, DBSchemas.User.USER_ID + " = ?", new String[]{cursor.getString(0)});
        } else {
            getContentResolver().insert(DBContentProvider.USER_URI, cv);
        }

        try {
            sender.sendGcmId(intent.getStringExtra("data"));
        } catch (ServerConnectException | InitialServerException e) {
            e.printStackTrace();
        } catch (BadSessionIdException | BadRequestException e) {
            e.printStackTrace();
        }
    }

    private void checkServerConnect(Intent intent) {
        if (sender == null) {
            Cursor cursor = getContentResolver().query(DBContentProvider.USER_URI, new String[]{DBSchemas.User.USER_ID}, null, null, null);
            if (cursor.moveToFirst()) {
                sender = new APISender(cursor.getInt(0));
            }
        }
        try {
            sender.sendCheckRequest();
        } catch (ServerConnectException | InitialServerException e) {
            e.printStackTrace();
        }
    }

    private void sendAPIRequest(Intent intent) {
        if (sender == null) {
            Cursor cursor = getContentResolver().query(DBContentProvider.USER_URI, new String[]{DBSchemas.User.USER_ID}, null, null, null);
            if (cursor.moveToFirst()) {
                sender = new APISender(cursor.getInt(0));
            }
        }
        NmapScanResult result = null;
        try {
            result = sender.sendAPIRequest(intent.getStringExtra("data"));
        } catch (ServerConnectException e) {
            e.printStackTrace();
        } catch (InitialServerException e) {
            e.printStackTrace();
        } catch (BadSessionIdException e) {
            e.printStackTrace();
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        if (result != null) {
            //----------- save scan --------------
//            ContentValues cv = new ContentValues();
//            cv.put(DBSchemas.Scans.TIME, result.getStart().toString());
//            cv.put(DBSchemas.Scans.COMMAND, result.getArgs());
//            getContentResolver().insert(DBContentProvider.SCANS_URI, cv);

            //------------ save temp_scan -------------

            //------------ save hosts -------------

            //------------ save host_names -------------

            //------------ save ports -------------

        }
    }
}
