package at.pranjic.server.http;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String name;

    HttpMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
