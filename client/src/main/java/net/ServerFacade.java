package net;
import com.google.gson.Gson;
import requests.*;
import results.*;
import exception.ResponseException;


import java.io.*;
import java.net.*;


public class ServerFacade {
    //this contains login register this will have 7 methods
    //this will have code that calls client communicator
    //use petshop and include issucces, readbody, throw, write , makerequest
    private String serverUrl;

    public ServerFacade (String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public RegisterResult register(RegisterRequest request) {
        var path = "/user";
        return this.makeRequest("POST", path, request, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request) {
        var path = "/session";
        return this.makeRequest("POST", path, request, LoginResult.class);
    }

    public LogoutResult logout(LogoutRequest request) {
        var path = "/session";
        return this.makeRequest("DELETE", path, request, LogoutResult.class);
    }

    public GameResult list(LogoutRequest request) {
        var path = "/game";
        return this.makeRequest("GET", path, request, GameResult.class);
    }

    public JoinResult join(JoinRequest request) {
        var path = "/game";
        return this.makeRequest("PUT", path, request, JoinResult.class);
    }

    public CreateResult create(CreateRequest request) {
        var path = "/game";
        return this.makeRequest("POST", path, request, CreateResult.class);
    }

    public void clear(CreateRequest request) {
        var path = "/db";
        this.makeRequest("DELETE", path, request, CreateResult.class); //probably doesnt return create
    }

    //from petshop
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }
    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}
