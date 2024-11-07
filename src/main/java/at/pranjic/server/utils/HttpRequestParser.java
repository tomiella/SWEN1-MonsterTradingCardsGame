package at.pranjic.server.utils;

import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.Request;

import java.util.Objects;

public class HttpRequestParser {
    public Request parse(String raw) {
        Request request = new Request();
        StringBuilder body = new StringBuilder();

        String[] rows = raw.split("\r\n");

        String[] requestLine = rows[0].split(" ");
        request.setMethod(HttpMethod.valueOf(requestLine[0]));
        request.setPath(requestLine[1]);

        boolean hasBody = false;
        for (int i = 1; i < rows.length; i++) {
            if (!hasBody) {
                String[] line = rows[i].split(": ", 2);
                if (Objects.equals(rows[i], "")) {
                    hasBody = true;
                    continue;
                }
                request.setHeader(line[0].toLowerCase(), line[1]);
            } else {
                body.append(rows[i]);
                body.append("\r\n");
            }
        }

        request.setBody(body.toString());

        return request;
    }

}
