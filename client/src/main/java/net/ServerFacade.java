package net;
import requests.*;
import results.*;
import net.ClientCommunicator;
import dataaccess.ResponseException;

public class ServerFacade {
    //this contains login register this will have 7 methods
    //this will have code that calls client communicator
    private final String serverUrl;
    private ClientCommunicator comms;

    public ServerFacade (String serverUrl) {
        this.serverUrl = serverUrl;
        this.comms = new ClientCommunicator();
    }

    public RegisterResult register(RegisterRequest request) throws ResponseException {
        var path = "/user";
        return comms.makeRequest("POST", path, request, RegisterResult.class,serverUrl, null);
    }

    public LoginResult login(LoginRequest request) throws ResponseException {
        var path = "/session";
        return comms.makeRequest("POST", path, request, LoginResult.class,serverUrl,null);
    }

    public LogoutResult logout(LogoutRequest request) throws ResponseException {
        var path = "/session";
        return comms.makeRequest("DELETE", path, null, LogoutResult.class,serverUrl,request.authToken());
    }

    public ListResult list(ListRequest request) throws ResponseException {
        var path = "/game";
        return comms.makeRequest("GET", path, null, ListResult.class,serverUrl,request.authToken());
    }

    public JoinResult join(JoinRequest request) throws ResponseException {
        var path = "/game";
        return comms.makeRequest("PUT", path, request, JoinResult.class,serverUrl,request.authToken());
    }

    public CreateResult create(CreateRequest request) throws ResponseException {
        var path = "/game";
        return comms.makeRequest("POST", path, request, CreateResult.class,serverUrl,request.authToken());
    }

    public void clear(CreateRequest request) throws ResponseException {
        var path = "/db";
        comms.makeRequest("DELETE", path, request, CreateResult.class,serverUrl,null); //probably doesnt return create
    }
}
