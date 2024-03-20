package handler;

import service.DatabaseService;
import service.exceptions.ServerErrorException;

public class ClearHandler extends Handler {
	final DatabaseService databaseService = DatabaseService.getInstance();

	public void handleClear(spark.Response response) {
		System.out.println("Created new clear handler");

		try {
			databaseService.clear();
			response.status(200);
			response.body("");
		} catch (ServerErrorException e) {
			serializeError(e, 500, response);
		}
	}
}
