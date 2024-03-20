package api;

import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.ErrorResponse;
import model.response.LoginResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.response.RegisterResponse;

public class UserHTTPHandler {
    int port;
    HTTPConnectionManager httpConnectionManager;
    UserHTTPHandler(int port) {
        this.port = port;
        httpConnectionManager = new HTTPConnectionManager(port);
    }
    public void login(LoginRequest loginRequest) throws HTTPResponseException, HTTPConnectionException {
        HashMap<String, List<String>> headers = new HashMap<>();
        ArrayList<String> contentTypes = new ArrayList<>();
        contentTypes.add("application/json");
        headers.put("Content-Type", contentTypes);

        LoginResponse loginResponse;

        try {
            HttpURLConnection connection = httpConnectionManager.getConnection(httpConnectionManager.getServerURLString() + "/session",
                    "POST", headers, true, true);

            httpConnectionManager.writeRequestBody(loginRequest, connection);

            if (HTTPConnectionManager.httpStatusIsOkay(connection)) {
                loginResponse = httpConnectionManager.readResponseBody(LoginResponse.class, connection);
            }
            else {
                ErrorResponse errorResponse = httpConnectionManager.readErrorBody(connection);
                throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
            }

            httpConnectionManager.updateAuthToken(loginResponse.authToken());
        } catch (IOException e) {
            throw new HTTPConnectionException(e.getMessage());
        }
    }



    public void register(RegisterRequest registerRequest) throws HTTPResponseException, HTTPConnectionException {
        HashMap<String, List<String>> headers = new HashMap<>();
        ArrayList<String> contentTypes = new ArrayList<>();
        contentTypes.add("application/json");
        headers.put("Content-Type", contentTypes);

        RegisterResponse registerResponse;
        try {
            HttpURLConnection connection = httpConnectionManager.getConnection(httpConnectionManager.getServerURLString() + "/user",
                    "POST", headers, true, true);

            httpConnectionManager.writeRequestBody(registerRequest, connection);

            if (HTTPConnectionManager.httpStatusIsOkay(connection)) {
                registerResponse = httpConnectionManager.readResponseBody(RegisterResponse.class, connection);
            }
            else {
                ErrorResponse errorResponse = httpConnectionManager.readErrorBody(connection);
                throw new HTTPResponseException(connection.getResponseCode(), errorResponse.message());
            }

            httpConnectionManager.updateAuthToken(registerResponse.authToken());
        }
        catch (IOException e) {
            throw new HTTPConnectionException(e.getMessage());
        }
    }

}
