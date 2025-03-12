package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorStatusMessage;
import dataaccess.ResponseException;
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

    public Object handle(Request req, Response res) {
        String authToken = req.headers("authorization");
        ListRequest request = new ListRequest(authToken);

        try {
            ListResult result = gameService.listGames(request);
            res.status(200);
            return new Gson().toJson(result);
        } catch( DataAccessException e ) {
            ErrorMessages errorMessage = new ErrorMessages();
            return new Gson().toJson(errorMessage.errorMessages(e,res));
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }

    };
}
