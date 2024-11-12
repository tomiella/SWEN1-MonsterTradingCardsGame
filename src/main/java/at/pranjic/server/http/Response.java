package at.pranjic.server.http;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private HttpStatus status;
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
