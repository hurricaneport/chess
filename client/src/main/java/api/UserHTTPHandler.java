package api;

import com.google.gson.Gson;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.response.ErrorResponse;
import model.response.LoginResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import api.HTTPConnectionManager;
import model.response.RegisterResponse;

public class UserHTTPHandler {
    Gson gson = new Gson();
    int port;
    HTTPConnectionManager httpConnectionManager;
    UserHTTPHandler(int port) {
        this.port = port;
        httpConnectionManager = new HTTPConnectionManager(port);
    }
    public LoginResponse login(LoginRequest loginRequest) throws HTTPResponseException, HTTPConnectionException {
        HashMap<String, List<String>> headers = new HashMap<>();
        ArrayList<String> contentTypes = new ArrayList<>();
        contentTypes.add("application/json");
        headers.put("Content-Type", contentTypes);

        LoginResponse loginResponse;

        try {
            HttpURLConnection connection = httpConnectionManager.getConnection(httpConnectionManager.getServerURLString() + "/session", "POST", headers, true, true);

            httpConnectionManager.writeRequestBody(loginRequest, connection);

            if (connection.getResponseCode() == 200) {
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
        return loginResponse;
    }


}
