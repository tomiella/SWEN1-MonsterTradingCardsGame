package at.pranjic.server.utils;

import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Response;

import java.util.Map;

public class HttpResponseFormatter {
    public String format(Response response) {
        if (response.getStatus() == null) {
           throw new NoHttpStatusException("No HTTP Status found");
        }

        StringBuilder responseString = new StringBuilder();

        responseString.append("HTTP/1.1 ");
        responseString.append(response.getStatus().toString());
        responseString.append("\r\n");

        Map<String, String> headers = response.getHeaders();

        if (response.getBody() != null) {
            headers.put("Content-Length", String.valueOf(response.getBody().length()));
        }

        headers.forEach((header, value) -> {
            responseString.append(header);
            responseString.append(": ");
            responseString.append(value);
            responseString.append("\r\n");
        });

        responseString.append("\r\n");

        if (response.getBody() != null) {
            responseString.append(response.getBody());
        }

        responseString.append("\r\n");

        return responseString.toString();
    }
}
