package at.pranjic.server.http;

import java.util.HashMap;

public class Response {

    private HttpStatus status;
    private HashMap<String, String> headers = new HashMap<>();
    private String body;

    @Override
    public String toString() {
        // TODO: make to string method
        return "";
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
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
