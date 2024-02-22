package server.handler;

import com.google.gson.Gson;
import server.ErrorResponse;
import server.RegisterResponse;
import server.Request;
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
     * turns server.Response object into JSON body and puts it into HTTP response body
     * @param serviceResponse response from service to be converted to JSON
     * @param serverResponse HTTP response with body to be set with JSON string
     */
    public static void serialize(Response serviceResponse, spark.Response serverResponse) {
        String responseBody = gson.toJson(serviceResponse);
        serverResponse.body(responseBody);
    }

    /**
     * Serializes response and adds proper status
     * @param serviceResponse response from the server
     * @param serverResponse response to send to client
     * @param expectedType expected type of serviceResponse (THIS SHOULD BE FIXED WITH EXCEPTIONS
     * @param <T>
     */
    public static <T> void serializeResponse(Response serviceResponse, spark.Response serverResponse, Class<T> expectedType) {
        if (serviceResponse.getClass() == expectedType) {
            serverResponse.status(200);
            serialize(serviceResponse, serverResponse);
        }
        else if(serviceResponse.getClass() == ErrorResponse.class) {
            serverResponse.status(((ErrorResponse) serviceResponse).status());
            ErrorResponse errorResponse = new ErrorResponse(((ErrorResponse) serviceResponse).message(), null);
            serialize(errorResponse, serverResponse);
        }
    }
}
