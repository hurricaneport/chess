package jsonUtils;

import com.google.gson.*;
import webSocketMessages.serverMessages.ErrorServerMessage;
import webSocketMessages.serverMessages.LoadGameServerMessage;
import webSocketMessages.serverMessages.NotificationServerMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.lang.reflect.Type;

public class GsonFactory {

	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ServerMessage.class, new ServerMessageDeserializer());
		gsonBuilder.registerTypeAdapter(UserGameCommand.class, new UserGameCommandDeserializer());
		gson = gsonBuilder.create();
	}

	private static final Gson gson;

	public static Gson getGson() {
		return gson;
	}

	private static class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {
		@Override
		public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			ServerMessage.ServerMessageType serverMessageType = ServerMessage.ServerMessageType.valueOf(jsonObject.get("serverMessageType").getAsString());

			return switch (serverMessageType) {
				case LOAD_GAME -> jsonDeserializationContext.deserialize(jsonElement, LoadGameServerMessage.class);
				case ERROR -> jsonDeserializationContext.deserialize(jsonElement, ErrorServerMessage.class);
				case NOTIFICATION ->
						jsonDeserializationContext.deserialize(jsonElement, NotificationServerMessage.class);

			};
		}
	}

	private static class UserGameCommandDeserializer implements JsonDeserializer<UserGameCommand> {
		@Override
		public UserGameCommand deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(jsonObject.get("commandType").getAsString());

			return switch (commandType) {
				case JOIN_PLAYER ->
						jsonDeserializationContext.deserialize(jsonElement, JoinPlayerUserGameCommand.class);
				case JOIN_OBSERVER ->
						jsonDeserializationContext.deserialize(jsonElement, JoinObserverUserGameCommand.class);
				case MAKE_MOVE -> jsonDeserializationContext.deserialize(jsonElement, MakeMoveUserGameCommand.class);
				case LEAVE -> jsonDeserializationContext.deserialize(jsonElement, LeaveUserGameCommand.class);
				case RESIGN -> jsonDeserializationContext.deserialize(jsonElement, ResignUserGameCommand.class);
			};
		}
	}
}


