package com.adojos.app;

import java.net.URI;

import com.adojos.app.context.AnnotationApplicationContext;
import com.adojos.app.http.HttpRequest;
import com.adojos.app.http.HttpResponse;
import com.adojos.app.http.WebMethod;

public class CommandLineApp {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException(
                    "Usage: java -cp target/classes com.adojos.app.CommandLineApp <controller-class> <path>");
        }

        System.out.println(execute(args[0], args[1]));
    }

    static String execute(String controllerClassName, String pathWithQuery) throws Exception {
        AnnotationApplicationContext context = new AnnotationApplicationContext();
        Class<?> controllerClass = Class.forName(controllerClassName);
        context.load(controllerClass);

        URI requestUri = new URI(pathWithQuery);
        String routePath = requestUri.getPath();
        WebMethod route = context.getRoutes().get(routePath);

        if (route == null) {
            throw new IllegalArgumentException("No @GetMapping found for path: " + routePath);
        }

        return route.execute(new HttpRequest(requestUri), new HttpResponse());
    }
}