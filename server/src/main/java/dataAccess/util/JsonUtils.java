package dataAccess.util;

import chess.ChessGame;
import com.google.gson.Gson;

public class JsonUtils {
    public static String serializeChessGame(ChessGame game) {
        Gson serializer = new Gson();
        return serializer.toJson(game);
    }

    public static ChessGame deserializeChessGame(String json) {
        Gson deserializer = new Gson();
        return deserializer.fromJson(json, ChessGame.class);
    }
}
