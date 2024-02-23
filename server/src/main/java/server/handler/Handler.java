package server.handler;

import com.google.gson.Gson;
import server.ErrorResponse;
import server.Response;

public abstract class Handler {
    private static final Gson gson = new Gson();

    /**
     * turns HTTP Request body into class
     * @param serverRequest HTTP request with JSON body
     */
    public static <T> T deserialize(spark.Request serverRequest, Class<T> obectClass) {
        return gson.fromJson(serverRequest.body(), obectClass);
    }

    /**
     * turns server.Response object into JSON body and puts it into HTTP response body. Adds status 200.
     * @param serviceResponse response from service to be converted to JSON
     * @param serverResponse HTTP response with body to be set with JSON string
     */
    public static void serialize(Response serviceResponse, spark.Response serverResponse) {
        String responseBody = gson.toJson(serviceResponse);
        serverResponse.body(responseBody);
        serverResponse.status(200);
    }

    /**
     * serializes error with given status and adds it to response body
     * @param e HTTP Exception
     * @param status status code
     * @param serverResponse response to add body and status to
     */
    public static void serializeError(Exception e, int status, spark.Response serverResponse) {
        String responseBody = gson.toJson(new ErrorResponse(e.getMessage()));
        serverResponse.body(responseBody);
        serverResponse.status(status);

    }
}
