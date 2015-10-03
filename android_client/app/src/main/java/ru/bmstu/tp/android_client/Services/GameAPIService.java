package ru.bmstu.tp.android_client.Services;

import android.app.IntentService;
import android.content.Intent;

import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

public class GameAPIService extends IntentService {
    public enum Action {
        GAME_REQUEST
    }

    public GameAPIService() {
        super("GameAPIService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch (Action.valueOf(intent.getAction())) {
            case GAME_REQUEST :
                sendRequest(intent);
                break;
        }
    }

    private void sendRequest(Intent intent) {
        AsyncHttpClient.getDefaultInstance().websocket(APISender.SERVER_URL, null, new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                webSocket.send("test data!!!");
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    public void onStringAvailable(String s) {
                        System.out.println("I got a string: " + s);
                    }
                });
                webSocket.setDataCallback(new DataCallback() {
                    public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
                        System.out.println("I got some bytes!");
                        // note that this data has been read
                        byteBufferList.recycle();
                    }
                });
            }
        });
    }
}
