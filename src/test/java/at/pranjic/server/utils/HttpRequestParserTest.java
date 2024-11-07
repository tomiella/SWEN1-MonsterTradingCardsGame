package at.pranjic.server.utils;

import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestParserTest {

    private final static String HTTP_GET = """
            GET /home HTTP/1.1
            Host: localhost:10001
            Authentication: Bearer example-token
            
            """.replace("\n","\r\n");

    private final static String HTTP_POST = """
            POST /users HTTP/1.1
            Host: localhost:8080
            Content-Type: application/json
            Content-Length: 30
            
            {
                "username": "example",
            }
            """.replace("\n","\r\n");

    private final HttpRequestParser requestParser = new HttpRequestParser();

    @Test
    public void give_httpGetRequest_then_parseMethod() {
        Request request = requestParser.parse(HTTP_GET);

        assertEquals(HttpMethod.GET, request.getMethod());
    }

    @Test
    public void give_httpGetRequest_then_parsePath() {
        Request request = requestParser.parse(HTTP_GET);

        assertEquals("/home", request.getPath());
    }

    @Test
    public void give_httpGetRequest_then_parseHostHeader() {
        Request request = requestParser.parse(HTTP_GET);

        assertEquals("localhost:10001", request.getHeader("host"));
    }

    @Test
    public void give_httpGetRequest_then_parseAuthToken() {
        Request request = requestParser.parse(HTTP_GET);

        assertEquals("Bearer example-token", request.getHeader("authentication"));
    }
}