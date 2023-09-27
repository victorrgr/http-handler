package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import static http.Constants.*;

public class Main {

    public static final int PORT = 8080;

    public static void main(String[] args) {
        var manager = new HttpPathManager();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Listening to port %s".formatted(PORT));
            try (var socket = serverSocket.accept();
                 var is = socket.getInputStream();
                 var os = socket.getOutputStream()) {
//                logger.info(new String(is.readAllBytes(), StandardCharsets.UTF_8));
                var response = manager.handle(new HttpRequest(is));
                os.write(response.bundle());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("Finished");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}