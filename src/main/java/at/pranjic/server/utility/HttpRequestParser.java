package at.pranjic.server.utility;

import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.Request;

import java.util.Objects;

public class HttpRequestParser {
    public Request parse(String raw) {
        Request request = new Request();
        StringBuilder body = new StringBuilder();

        String[] rows = raw.split("\n");

        String[] actionRow = rows[0].split(" ");
        request.setMethod(HttpMethod.valueOf(actionRow[0]));
        request.setPath(actionRow[1]);

        boolean hasBody = false;
        for (int i = 1; i < rows.length; i++) {
            if (!hasBody) {
                String[] line = rows[i].split(": ");
                if (Objects.equals(rows[i], "")) {
                    hasBody = true;
                    continue;
                }
                request.setHeader(line[0], line[1]);
            } else {
                body.append(rows[i]);
            }
        }

        request.setBody(body.toString());

        return null;
    }

    public static void main(String[] args) {
        String raw = """
POST /path/resource HTTP/1.1
Host: www.example.com
Content-Type: application/json
Content-Length: 74

{
    "key1": "value1",
    "key2": "value2",
    "key3": "value3"
}
                """;

        HttpRequestParser p = new HttpRequestParser();
        p.parse(raw);
    }
}
