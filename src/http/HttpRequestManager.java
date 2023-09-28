package http;

import http.controllers.GeneralController;
import http.mappings.Controller;
import http.mappings.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static http.Constants.*;

public class HttpRequestManager {
    private List<Mapping> mappings;

    public HttpRequestManager() {
        mappings = new ArrayList<>();
//        pathMappings = new HashMap<>();
//        pathMappings.put("/helloWorld", new GeneralController());
        testSomething();
    }

    private void testSomething() {
//        var packages = new String[] {
//                "http.controllers"
//        };
//        var packageName = packages[0];
        this.mappings = findClasses("http.controllers").stream()
                .filter(c -> c.isAnnotationPresent(Controller.class))
                .flatMap(c -> {
                    try {
                        var obj = c.getDeclaredConstructor().newInstance();
                        var controllerAnnotation = c.getAnnotation(Controller.class);
                        return Arrays.stream(c.getMethods())
                                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                                .map(m -> {
                                    var requestAnnotation = m.getAnnotation(RequestMapping.class);
                                    return new Mapping(obj, m, requestAnnotation.method(), controllerAnnotation.value().concat(requestAnnotation.path()));
                                });
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                             IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    record Mapping(Object instance, Method method, String httpMethod, String httpPath) {
    }

    private static Set<Class<?>> findClasses(String packageName) {
        try (var inputStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"))) {
            assert inputStream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(l -> packageName
                            + "."
                            + l.substring(0, l.lastIndexOf('.')))
                    .map(className -> {
                        try {
                            return Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            logger.warning(() -> "SingletonEngine failed to getClass, message: " + e.getMessage());
                            return null;
                        }
                    })
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public HttpResponse handle(HttpRequest httpRequest) {
//        var handler = pathMappings.get(httpRequest.getPath());
        var handler = findMapping(httpRequest);
        if (handler == null) {
            Set<String> headers = new HashSet<>();
            headers.add("Content-Type: application/json");
            String content = "{ \"message\": \"Requested path is not configured.\" }";
            return new HttpResponse(HTTP_VERSION, NOT_FOUND, headers, content);
        }
        try {
            return (HttpResponse) handler.method().invoke(handler.instance(), httpRequest);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private Mapping findMapping(HttpRequest httpRequest) {
        return mappings.stream()
                .filter(o -> o.httpMethod().equals(httpRequest.getMethod())
                        && o.httpPath().equals(httpRequest.getPath()))
                .findFirst()
                .orElse(null);
    }
}
