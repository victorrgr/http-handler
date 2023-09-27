package http;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import static http.Constants.*;

public class Main {

    public static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Listening to port %s".formatted(PORT));
            try (var socket = serverSocket.accept();
                 var is = socket.getInputStream();
                 var os = socket.getOutputStream()) {
//                logger.info(new String(is.readAllBytes(), StandardCharsets.UTF_8));
                var request = new HttpRequest(is);
                String responseContent = "{ \"status\": \"OK\" }";
                Set<String> headers = new HashSet<>();
                headers.add("Content-Type: application/json");
                var response = new HttpResponse(HTTP_VERSION, OK, headers, responseContent);
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