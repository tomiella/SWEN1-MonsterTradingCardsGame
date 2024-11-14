package at.pranjic.server;

import at.pranjic.server.http.Request;
import at.pranjic.server.http.Response;
import at.pranjic.server.utils.HttpRequestParser;
import at.pranjic.server.utils.HttpResponseFormatter;
import at.pranjic.server.utils.HttpSocket;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;

public class RequestHandler {
    private final Socket socket;
    private final Application application;

    public RequestHandler(Socket socket, Application application) {
        this.socket = socket;
        this.application = application;
    }

    public void handle() {
        try(HttpSocket httpSocket = new HttpSocket(this.socket)) {

            String http = httpSocket.readHttp();

            HttpRequestParser httpRequestParser = new HttpRequestParser();
            Request request = httpRequestParser.parse(http);

            System.out.printf("%s %s %s\n", request.getMethod(), request.getPath(), LocalDateTime.now().toString());

            Response response = this.application.handle(request);

            HttpResponseFormatter httpResponseFormatter = new HttpResponseFormatter();
            String responseRaw = httpResponseFormatter.format(response);

            httpSocket.writeHttp(responseRaw);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
