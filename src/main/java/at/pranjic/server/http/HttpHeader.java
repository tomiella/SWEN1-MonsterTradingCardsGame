package at.pranjic.server.http;

public enum HttpHeader {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    AUTHORIZATION("Authorization"),
    ACCEPT("Accept"),
    ACCEPT_ENCODING("Accept-Encoding"),
    USER_AGENT("User-Agent"),
    HOST("Host"),
    CACHE_CONTROL("Cache-Control"),
    CONNECTION("Connection"),
    COOKIE("Cookie"),
    SET_COOKIE("Set-Cookie"),
    REFERER("Referer"),
    ORIGIN("Origin"),
    ACCEPT_LANGUAGE("Accept-Language");

    private final String headerName;

    HttpHeader(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderName() {
        return headerName;
    }

    @Override
    public String toString() {
        return headerName;
    }
}
