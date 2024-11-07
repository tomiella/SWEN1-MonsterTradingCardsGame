package at.pranjic.server.http;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private HttpMethod method;
    private String path;
    private final Map<String, String> headers = new HashMap<>();
    private String body;


    public void parse() {
        // TODO: parse string
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public void setHeader(String header, String value) {
        headers.put(header, value);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
