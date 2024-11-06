package at.pranjic.server;

import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;

public interface Application {
    Response handle(Request request);
}
