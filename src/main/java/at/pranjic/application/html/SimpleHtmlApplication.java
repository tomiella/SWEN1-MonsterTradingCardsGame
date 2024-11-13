package at.pranjic.application.html;

import at.pranjic.server.Application;
import at.pranjic.server.http.HttpStatus;
import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;

public class SimpleHtmlApplication implements Application {
    @Override
    public Response handle(Request request) {
        Response response = new Response();
        response.setStatus(HttpStatus.OK);
        response.setBody("Hello Wordl");

        return response;
    }
}
