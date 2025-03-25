package net;
import requests.RegisterRequest;
import results.RegisterResult;

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

        return null;
    }

    public void login() {

    }

    public void logout() {

    }

    public void list() {

    }

    public void join() {

    }

    public void create() {

    }

    public void game() {

    }

}
