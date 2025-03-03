package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ErrorStatusMessage;
import spark.Response;
import java.util.Objects;

public class ErrorMessages {
    public ErrorStatusMessage ErrorMessage(DataAccessException e, Response res){
        if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
            res.status(401);
            return new ErrorStatusMessage("401", e.getMessage());
        } else {
            res.status(500);
            return new ErrorStatusMessage("500", e.getMessage());
        }
    }
}
