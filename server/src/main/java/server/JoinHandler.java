package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorStatusMessage;
import requests.ColorGameIDRequest;
import requests.JoinRequest;
import results.JoinResult;
import results.ListResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class JoinHandler implements Route {
    private GameService gameService;

    public JoinHandler(GameService gameService) {
        this.gameService = gameService;
    }

    ;

    public Object handle(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        ColorGameIDRequest colorID = new Gson().fromJson(req.body(), ColorGameIDRequest.class);

        JoinRequest request = new JoinRequest(authToken, colorID.playerColor(),colorID.gameID());

        try {
            if(request.playerColor() == null | request.gameID() == null) {
                res.status(400);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("400", "Error: bad request");
                return new Gson().toJson(errorResponse);
            }
            JoinResult result = gameService.join(request);
            res.status(200);
            return new Gson().toJson(result);
        } catch ( DataAccessException e) {
            if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
                res.status(401);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("401", e.getMessage());
                return new Gson().toJson(errorResponse);
            } else if (Objects.equals(e.getMessage(), "Error: bad request")) {
                res.status(400);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("400", e.getMessage());
                return new Gson().toJson(errorResponse);
            } else if (Objects.equals(e.getMessage(), "Error: already taken")) {
                res.status(400);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("400", e.getMessage());
                return new Gson().toJson(errorResponse);
            } else {
                res.status(500);
                ErrorStatusMessage errorResponse = new ErrorStatusMessage("500", e.getMessage());
                return new Gson().toJson(errorResponse);
            }
        }
    }
}
