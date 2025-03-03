package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import requests.CreateRequest;
import results.CreateResult;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateHandler implements Route {
    private UserService userService;

    public CreateHandler(UserService userService) {
        this.userService = userService;
    };

    public Object handle(Request req, Response res) throws DataAccessException {
        CreateRequest request = new Gson().fromJson(req.body(), CreateRequest.class);

        try {