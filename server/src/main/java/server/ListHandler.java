package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorStatusMessage;
import requests.GameNameRequest;
import requests.ListRequest;
import results.ListResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class ListHandler implements Route {
    private GameService gameService;

    public ListHandler(GameService gameService) {
        this.gameService = gameService;
    };

    public Object handle(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        ListRequest request = new ListRequest(authToken);

        try {
            ListResult result = gameService.listGames(request);
            res.status(200);
            return new Gson().toJson(result);
        } catch( DataAccessException e ) {
            ErrorStatusMessage errorResponse = null;
            if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
                res.status(401);
                errorResponse = new ErrorStatusMessage("401", e.getMessage());
                //return new Gson().toJson(errorResponse);
            } else {
                res.status(500);
                errorResponse = new ErrorStatusMessage("500", e.getMessage());
                //return new Gson().toJson(errorResponse);
            }
            return new Gson().toJson(errorResponse);

        }

    };
}
