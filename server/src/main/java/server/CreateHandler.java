package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorStatusMessage;
import requests.CreateRequest;
import results.CreateResult;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.xml.crypto.Data;

public class CreateHandler implements Route {
    private GameService gameService;

    public CreateHandler(GameService gameService) {
        this.gameService = gameService;
    };

    public Object handle(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        CreateRequest request = new Gson().fromJson(req.body(), CreateRequest.class);

        try {
            if (request.gameName() == null) {
                res.status(400);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("400", "Error: bad request");
                return new Gson().toJson(errorResponse);
            }
            if (authToken == null) {
                res.status(400);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("400", "Error: bad request");
                return new Gson().toJson(errorResponse);
            }

            CreateResult result = gameService.create(request);
            res.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException e) {
            res.status(500);
            ErrorStatusMessage errorResponse = new ErrorStatusMessage("500", e.getMessage());
            return new Gson().toJson(errorResponse);
        }
    };
}