package at.pranjic.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private Application application;

    private ServerSocket serverSocket;

    public Server(Application application) {
        this.application = application;
    }

    public void start() {
        try {
            this.serverSocket = new ServerSocket(10001);

            System.out.println("Server started on port 10001");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ExecutorService threadPool = Executors.newFixedThreadPool(10)) {
            while (true) {
                try {
                    Socket socket = this.serverSocket.accept();
                    RequestHandler requestHandler = new RequestHandler(socket, application);
                    threadPool.submit(requestHandler);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
