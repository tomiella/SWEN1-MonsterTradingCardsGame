package at.pranjic.server.utils;

import at.pranjic.server.http.Response;
import at.pranjic.server.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpResponseFormatterTest {

    private final HttpResponseFormatter httpResponseFormatter = new HttpResponseFormatter();

    @Test
    public void given_statusOk_when_useFormatter_then_formatStatusLineCorrectly() {
        Response response = new Response();
        response.setStatus(HttpStatus.OK);

        String http = httpResponseFormatter.format(response);

        assertTrue(http.startsWith("HTTP/1.1 200 OK\r\n"));
    }

    @Test
    public void given_statusInternalServerError_when_useFormatter_then_formatStatusLineCorrectly() {
        Response response = new Response();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        String http = httpResponseFormatter.format(response);

        assertTrue(http.startsWith("HTTP/1.1 500 Internal Server Error\r\n"));
    }

    @Test
    public void given_authenticationHeader_when_useFormatter_then_formatHeaderCorrectly() {
        Response response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setHeader("Authentication", "Bearer example-token");

        String http = httpResponseFormatter.format(response);

        assertTrue(http.contains("Authentication: Bearer example-token\r\n"));
    }

    @Test
    public void given_body_when_useFormatter_then_formatContentLengthCorrectly() {
        Response response = new Response();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setHeader("Content-Type", "application/json");
        String body = "{ \"message\": \"Internal Server Error\" }";
        response.setBody(body);

        String http = httpResponseFormatter.format(response);

        assertTrue(
                http.contains("Content-Length: %s\r\n".formatted(body.length()))
        );
    }

    @Test
    public void given_body_when_useFormatter_then_formatBodyCorrectly() {
        Response response = new Response();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setHeader("Content-Type", "application/json");
        String body = "{ \"message\": \"Internal Server Error\" }";
        response.setBody(body);

        String http = httpResponseFormatter.format(response);

        assertTrue(http.contains(body));
    }

    @Test
    public void given_body_when_useFormatter_then_formatEmptyLineCorrectly() {
        Response response = new Response();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setHeader("Content-Type", "application/json");
        String body = "{ \"message\": \"Internal Server Error\" }";
        response.setBody(body);

        String http = httpResponseFormatter.format(response);

        assertTrue(http.contains("\r\n%s".formatted(body)));
    }

    @Test
    public void given_noStatus_when_useFormatter_then_noHttpStatusException() {
        Response response = new Response();

        assertThrows(
                NoHttpStatusException.class,
                () -> httpResponseFormatter.format(response)
        );
    }
}