package http;

import java.util.*;

import static http.Constants.HTTP_VERSION;
import static http.Constants.NOT_FOUND;

public class HttpPathManager implements PathHandler {

    private final Map<String, PathHandler> pathMappings;
    public HttpPathManager() {
        pathMappings = new HashMap<>();
        pathMappings.put("/helloWorld", new GeneralPathHandler());
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        var handler = pathMappings.get(httpRequest.getPath());
        if (handler == null) {
            Set<String> headers = new HashSet<>();
            headers.add("Content-Type: application/json");
            String content = "{ \"message\": \"Requested path is not configured.\" }";
            return new HttpResponse(HTTP_VERSION, NOT_FOUND, headers, content);
        }
        return handler.handle(httpRequest);
    }
}
