package handler;

import service.exceptions.ServerErrorException;
import service.DatabaseService;

public class ClearHandler extends Handler{
    DatabaseService databaseService = DatabaseService.getInstance();
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
