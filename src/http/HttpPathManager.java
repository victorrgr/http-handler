package http;

import java.util.Map;

public class HttpPathManager implements PathHandler {

    private Map<String, PathHandler> pathMappings;
    public HttpPathManager() {
        pathMappings.put("/helloWorld", new GeneralPathHandler());
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
//        pathMappings.get(httpRequest.getPath());
        httpRequest.
        return null;
    }
}
