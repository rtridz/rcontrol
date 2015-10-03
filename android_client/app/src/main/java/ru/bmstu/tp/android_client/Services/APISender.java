package ru.bmstu.tp.android_client.Services;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import ru.bmstu.tp.android_client.Services.Exceptions.BadRequestException;
import ru.bmstu.tp.android_client.Services.Exceptions.BadUserIdException;
import ru.bmstu.tp.android_client.Services.Exceptions.ForbiddenException;
import ru.bmstu.tp.android_client.Services.Exceptions.InitialServerException;
import ru.bmstu.tp.android_client.Services.Exceptions.NonAuthoritativeException;
import ru.bmstu.tp.android_client.Services.Exceptions.NotFoundException;
import ru.bmstu.tp.android_client.Services.Exceptions.ServerConnectException;
import ru.bmstu.tp.android_client.Services.SQLReader.NmapScanResult;

public class APISender {
    public static final String SERVER_URL = "http://127.0.0.1:8080";

    private int userId;
    private String cookies = null;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public APISender(int userId) {
        this.userId = userId;
    }

    private StringBuilder sendRequest(StringBuilder url, String data) throws NonAuthoritativeException,
            BadRequestException, ForbiddenException, NotFoundException, InitialServerException, ServerConnectException {
        URL getReq;
        try {
            getReq = new URL(url.toString());
        } catch (MalformedURLException e) {
            return null;
        }
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) getReq.openConnection();
            if (data != null) {
                con.setDoOutput(true);
                con.setChunkedStreamingMode(0);
                OutputStream out = new BufferedOutputStream(con.getOutputStream());
                out.write(data.getBytes());
            }
        } catch (IOException e) {
            throw new ServerConnectException();
        }
        StringBuilder response = new StringBuilder();
        InputStream is = null;
        BufferedReader reader = null;
        try {
            is = con.getInputStream();
            if (con.getResponseCode() != 200) {
                switch (con.getResponseCode()) {
                    case 203:
                        throw new NonAuthoritativeException();
                    case 400:
                        throw new BadRequestException();
                    case 403:
                        throw new ForbiddenException();
                    case 404:
                        throw new NotFoundException();
                    case 500:
                        throw new InitialServerException();
                    default:
                        throw new InitialServerException();
                }
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            throw new ServerConnectException();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
            con.disconnect();
        }
        return response;
    }

    public void registrate(String name, String password) throws ServerConnectException, InitialServerException, BadRequestException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append("/regist");
        String data = String.format("{name: ?, password: ?}", name, password);
        StringBuilder response = null;
        try {
            response = sendRequest(url, data);
        } catch (NonAuthoritativeException | ForbiddenException | NotFoundException ignore) {}
        if (response == null || response.length() == 0) {
            throw new ServerConnectException();
        }
        try {
            JSONObject json = new JSONObject(response.toString());
            userId = (Integer) json.get("token");
        } catch (JSONException | IllegalArgumentException | ClassCastException e) {
            throw new InitialServerException();
        }
    }

    public void authorize(String name, String password) throws ServerConnectException, InitialServerException, BadRequestException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append("/auth");
        String data = String.format("{name: ?, password: ?}", name, password);
        StringBuilder response = null;
        try {
            response = sendRequest(url, data);
        } catch (NonAuthoritativeException | ForbiddenException | NotFoundException ignore) {}
        if (response == null || response.length() == 0) {
            throw new ServerConnectException();
        }
        try {
            JSONObject json = new JSONObject(response.toString());
            userId = (Integer) json.get("token");
        } catch (JSONException | IllegalArgumentException | ClassCastException e) {
            throw new InitialServerException();
        }
    }

    public void sendGcmId(String gcmId) throws ServerConnectException, InitialServerException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append("/gcm_id");
        String data = String.format("{token: ?, gcm_id: ?}", userId, gcmId);
        try {
            StringBuilder response = sendRequest(url, data);
        } catch (BadRequestException | ForbiddenException | NotFoundException | NonAuthoritativeException ignore) {}
    }

    public void checkConnect() throws ServerConnectException, InitialServerException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append("/check_connect");
        try {
            StringBuilder response = sendRequest(url, null);
        } catch (BadRequestException | ForbiddenException | NotFoundException | NonAuthoritativeException ignore) {}
    }
}
