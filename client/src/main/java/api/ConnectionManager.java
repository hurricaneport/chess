package api;

import com.google.gson.Gson;
import jsonUtils.GsonFactory;
import model.request.Request;
import model.response.ErrorResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Map;

public class ConnectionManager {
	private static String authToken = "";
	private final String serverUrl;

	private final Gson gson = GsonFactory.getGson();

	public ConnectionManager(int port) {
		serverUrl = "http://localhost:" + port;
	}

	public static String getAuthToken() {
		return authToken;
	}

	public static void clearAuthToken() {
		authToken = "";
	}

	public static void updateAuthToken(String newAuthToken) {
		authToken = newAuthToken;
	}

	public static boolean httpStatusIsOkay(HttpURLConnection connection) throws IOException {
		return connection.getResponseCode() == 200;
	}

	public HttpURLConnection getConnection(String endpoint, String requestMethod, Map<String, String> headers, boolean doOutput) throws IOException {
		URL url;
		endpoint = serverUrl + endpoint;
		try {
			url = (new URI(endpoint)).toURL();
		} catch (MalformedURLException | URISyntaxException e) {
			throw new RuntimeException(e);
		}

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(5000);
		connection.setRequestMethod(requestMethod);
		connection.setDoOutput(doOutput);
		connection.setDoInput(true);

		if (headers != null) {
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
		try (OutputStream requestBody = connection.getOutputStream()) {
			requestBody.write(gson.toJson(request).getBytes());
		}
	}

	public <T> T readResponseBody(Class<T> responseClass, HttpURLConnection connection) throws IOException {
		T response;
		try (InputStream responseBody = connection.getInputStream()) {
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
}
