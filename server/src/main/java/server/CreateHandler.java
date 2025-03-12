package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorStatusMessage;
import dataaccess.ResponseException;
import requests.CreateRequest;
import requests.GameNameRequest;
import results.CreateResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.Objects;

public class CreateHandler implements Route {
    private GameService gameService;

    public CreateHandler(GameService gameService) {
        this.gameService = gameService;
    };

    public Object handle(Request req, Response res) {
        String authToken = req.headers("authorization");
        GameNameRequest gameName = new Gson().fromJson(req.body(), GameNameRequest.class);

        CreateRequest request = new CreateRequest(gameName.gameName(), authToken);

        try {
            if (request.gameName() == null) {
                res.status(400);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("400", "Error: bad request");
                return new Gson().toJson(errorResponse);
            }

            CreateResult result = gameService.create(request);
            res.status(200);
            return new Gson().toJson(result);
        }
        catch (DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
                res.status(401);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("401", e.getMessage());
                return new Gson().toJson(errorResponse);
            } else {
                res.status(500);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("500", e.getMessage());
                return new Gson().toJson(errorResponse);
            }
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    };
}