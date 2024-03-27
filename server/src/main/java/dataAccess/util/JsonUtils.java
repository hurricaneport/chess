package dataAccess.util;

import chess.ChessGame;
import com.google.gson.Gson;
import jsonUtils.GsonFactory;

public class JsonUtils {
	public static String serializeChessGame(ChessGame game) {
		Gson serializer = GsonFactory.getGson();
		return serializer.toJson(game);
	}

	public static ChessGame deserializeChessGame(String json) {
		Gson deserializer = GsonFactory.getGson();
		return deserializer.fromJson(json, ChessGame.class);
	}
}
