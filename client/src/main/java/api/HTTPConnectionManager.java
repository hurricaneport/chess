package api;

import com.google.gson.Gson;
import model.request.Request;
import model.response.ErrorResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Map;

public class HTTPConnectionManager {
    private final String serverUrl;
    private static String authToken = "";
    Gson gson = new Gson();
    HTTPConnectionManager(int port) {
        serverUrl = "http://localhost:" + port;
    }

    public String getServerURLString() {
        return serverUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void clearAuthToken() {
        authToken = "";
    }

    public void updateAuthToken(String newAuthToken) {
        authToken = newAuthToken;
    }

    public HttpURLConnection getConnection(String urlString, String requestMethod, Map<String, String> headers, boolean doOutput, boolean doInput) throws IOException {
        URL url;
        try {
            url = (new URI(urlString)).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod(requestMethod);
        connection.setDoOutput(doOutput);
        connection.setDoInput(doInput);

        if(headers != null) {
            for (String headerKey : headers.keySet()) {
                if (headers.get(headerKey) != null) {
                    connection.addRequestProperty(headerKey, headers.get(headerKey));
                }
            }
        }

        connection.connect();
        return connection;
    }

    public void writeRequestBody(Request request, HttpURLConnection connection) throws IOException {
        try(OutputStream requestBody = connection.getOutputStream()) {
        requestBody.write(gson.toJson(request).getBytes());
        }
    }

    public <T> T readResponseBody(Class<T> responseClass, HttpURLConnection connection) throws IOException {
        T response;
        try(InputStream responseBody = connection.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(responseBody);
            response = gson.fromJson(reader, responseClass);
        }

        return response;
    }

    public ErrorResponse readErrorBody(HttpURLConnection connection) throws IOException {
        ErrorResponse errorResponse;
        try (InputStream errorBody = connection.getErrorStream()) {
            InputStreamReader reader = new InputStreamReader(errorBody);
            errorResponse = gson.fromJson(reader, ErrorResponse.class);
        }
        return errorResponse;
    }

    public static boolean httpStatusIsOkay(HttpURLConnection connection) throws IOException {
        return connection.getResponseCode() == 200;
    }
}
