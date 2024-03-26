package jsonUtils;

import com.google.gson.*;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

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
			return null;
		}
	}

	private static class UserGameCommandDeserializer implements JsonDeserializer<UserGameCommand> {
		@Override
		public UserGameCommand deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			return null;
		}
	}
}


