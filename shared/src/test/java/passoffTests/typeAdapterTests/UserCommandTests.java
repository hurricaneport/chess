package passoffTests.typeAdapterTests;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import jsonUtils.GsonFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webSocketMessages.userCommands.*;

public class UserCommandTests {
	private final Gson gson = GsonFactory.getGson();

	@Test
	@DisplayName("Join Player Command")
	public void joinPlayerCommand() {
		UserGameCommand userGameCommand = new JoinPlayerUserGameCommand("1234", 1234, ChessGame.TeamColor.BLACK);
		String json = gson.toJson(userGameCommand);

		UserGameCommand userGameCommand1 = gson.fromJson(json, UserGameCommand.class);
		Assertions.assertEquals(JoinPlayerUserGameCommand.class, userGameCommand1.getClass());
	}

	@Test
	@DisplayName("Join Observer Command")
	public void joinObserverCommand() {
		UserGameCommand userGameCommand = new JoinObserverUserGameCommand("1234", 1234);
		String json = gson.toJson(userGameCommand);

		UserGameCommand userGameCommand1 = gson.fromJson(json, UserGameCommand.class);
		Assertions.assertEquals(JoinObserverUserGameCommand.class, userGameCommand1.getClass());
	}

	@Test
	@DisplayName("Make Move Command")
	public void makeMoveCommand() {
		MakeMoveUserGameCommand userGameCommand = new MakeMoveUserGameCommand("1234", 1234, new ChessMove(new ChessPosition(1, 1), new ChessPosition(1, 1)));
		String json = gson.toJson(userGameCommand);

		UserGameCommand userGameCommand1 = gson.fromJson(json, UserGameCommand.class);
		Assertions.assertEquals(MakeMoveUserGameCommand.class, userGameCommand1.getClass());

		MakeMoveUserGameCommand makeMoveUserGameCommand1 = (MakeMoveUserGameCommand) userGameCommand1;
		Assertions.assertEquals(makeMoveUserGameCommand1.getChessMove(), userGameCommand.getChessMove());
	}

	@Test
	@DisplayName("Leave Game Command")
	public void leaveGameCommand() {
		UserGameCommand userGameCommand = new LeaveUserGameCommand("1234", 1234);
		String json = gson.toJson(userGameCommand);

		UserGameCommand userGameCommand1 = gson.fromJson(json, UserGameCommand.class);
		Assertions.assertEquals(LeaveUserGameCommand.class, userGameCommand1.getClass());
	}

	@Test
	@DisplayName("Resign Player Command")
	public void resignPlayerCommand() {
		UserGameCommand userGameCommand = new ResignUserGameCommand("1234", 1234);
		String json = gson.toJson(userGameCommand);

		UserGameCommand userGameCommand1 = gson.fromJson(json, UserGameCommand.class);
		Assertions.assertEquals(ResignUserGameCommand.class, userGameCommand1.getClass());
	}
}
