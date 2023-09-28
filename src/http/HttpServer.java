package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static http.Constants.logger;

public class HttpServer {
    private final ConcurrentLinkedQueue<Socket> queue;
    private final Integer port;
    private final HttpRequestManager manager;
    private final ExecutorService processor;

    public HttpServer(Integer port, Integer threads) {
        this.port = port;
        this.queue = new ConcurrentLinkedQueue<>();
        this.manager = new HttpRequestManager();
        this.processor = Executors.newFixedThreadPool(threads);
        process();
    }

    private void process() {
        logger.info(() -> "Listening to port " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!serverSocket.isClosed()) {
                var socket = serverSocket.accept();
                handleRequestEntry(socket);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        logger.info("Http Server is finished");
    }


    private void handleRequestEntry(Socket socket) {
        try {
            this.queue.add(socket);
            this.processor.submit(handleRequest());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Runnable handleRequest() {
        return () -> {
            try (var socket = queue.poll()) {
                if (socket == null)
                    return;
                try (var is = socket.getInputStream();
                     var os = socket.getOutputStream()) {
                    var response = manager.handle(new HttpRequest(is));
                    os.write(response.bundle());
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        };
    }

}
