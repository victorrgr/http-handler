package http.controllers;

import http.HttpRequest;
import http.HttpResponse;
import http.mappings.Controller;
import http.mappings.RequestMapping;

import java.util.HashSet;
import java.util.Set;

import static http.Constants.HTTP_VERSION;
import static http.Constants.OK;

@Controller("/api")
public class TestController {

    @RequestMapping(method = "GET", path = "/test")
    public HttpResponse test(HttpRequest httpRequest) {
        String responseContent = "{ \"status\": \"OK\" }";
        Set<String> headers = new HashSet<>();
        headers.add("Content-Type: application/json");
        return new HttpResponse(HTTP_VERSION, OK, headers, responseContent);
    }

}
