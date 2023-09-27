package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static http.Constants.CRLF;
import static http.Constants.logger;

public class HttpRequest {
    private final InputStream inputStream;
    private String requestLine;
    private String[] headerLines;
    private String requestBody;
    private String fullRequest;

    public HttpRequest(InputStream inputStream) {
        this.inputStream = inputStream;
        process();
    }

    private void process() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            // Read and store the request line
            String requestLine = reader.readLine();
            if (requestLine == null) {
                return; // No request line, exit
            }
            this.requestLine = requestLine;

            // Read and store the request headers
            StringBuilder headers = new StringBuilder();
            String headerLine;
            while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
                headers.append(headerLine).append(CRLF);
            }

            // Now headers contains the request headers

            // Check if there's a Content-Length header to determine if there's a request body
            int contentLength = 0;
            String[] headerLines = headers.toString().split(CRLF);
            for (String header : headerLines) {
                if (header.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(header.substring("Content-Length:".length()).trim());
                    break;
                }
            }
            this.headerLines = headerLines;

            // Read and store the request body, if present
            StringBuilder requestBody = new StringBuilder();
            if (contentLength > 0) {
                char[] buffer = new char[1024];
                int bytesRead;
                int totalBytesRead = 0;
                while (totalBytesRead < contentLength && (bytesRead = reader.read(buffer, 0, Math.min(buffer.length, contentLength - totalBytesRead))) != -1) {
                    requestBody.append(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                }
            }
            this.requestBody = requestBody.toString();

            // Now you have the request line, headers, and request body
            fullRequest = requestLine + CRLF + headers + CRLF + requestBody;
            logger.info(fullRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
