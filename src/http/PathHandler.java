package http;

public interface PathHandler {
    HttpResponse handle(HttpRequest httpRequest);
}
