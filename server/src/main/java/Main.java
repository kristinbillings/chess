import chess.*;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import server.Server;

public class Main {
    public static void main(String[] args) throws ResponseException, DataAccessException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        new Server().run(8080);
    }
}