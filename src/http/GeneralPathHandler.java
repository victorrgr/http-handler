package http;

import java.util.HashSet;
import java.util.Set;

import static http.Constants.HTTP_VERSION;
import static http.Constants.OK;

public class GeneralPathHandler implements PathHandler{
    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        String responseContent = "{ \"status\": \"OK\" }";
        Set<String> headers = new HashSet<>();
        headers.add("Content-Type: application/json");
        return new HttpResponse(HTTP_VERSION, OK, headers, responseContent);
    }
}
