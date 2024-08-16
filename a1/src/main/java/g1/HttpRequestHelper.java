package g1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestHelper {

    public static HttpResponse makeHttpRequest(String urlString, int timeout) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder responseContent = new StringBuilder();
        int statusCode = -1;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            statusCode = connection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new HttpResponse(statusCode, responseContent.toString());
    }

    public static class HttpResponse {
        private final int statusCode;
        private final String responseContent;

        public HttpResponse(int statusCode, String responseContent) {
            this.statusCode = statusCode;
            this.responseContent = responseContent;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getResponseContent() {
            return responseContent;
        }
    }
  }