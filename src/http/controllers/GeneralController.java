package http.controllers;

import http.HttpRequest;
import http.HttpResponse;
import http.PathHandler;

import java.util.HashSet;
import java.util.Set;

import static http.Constants.HTTP_VERSION;
import static http.Constants.OK;

public class GeneralController implements PathHandler {
    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        String responseContent = "{ \"status\": \"OK\" }";
        Set<String> headers = new HashSet<>();
        headers.add("Content-Type: application/json");
        return new HttpResponse(HTTP_VERSION, OK, headers, responseContent);
    }
}
