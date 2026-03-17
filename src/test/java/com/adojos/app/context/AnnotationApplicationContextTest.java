package com.adojos.app.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.junit.After;
import org.junit.Test;

import com.adojos.app.controllers.HelloController;
import com.adojos.app.http.HttpRequest;
import com.adojos.app.http.HttpResponse;
import com.adojos.app.http.HttpServer;

public class AnnotationApplicationContextTest {

    @After
    public void tearDown() {
        HttpServer.resetRoutesForTests();
    }

    @Test
    public void shouldDiscoverAnnotatedControllersFromClasspath() {
        HttpServer.resetRoutesForTests();
        HttpServer.loadAnnotationRoutes();

        assertTrue(HttpServer.hasRoute("/"));
        assertTrue(HttpServer.hasRoute("/greeting"));
    }

    @Test
    public void shouldResolveRequestParamWithDefaultValue() throws Exception {
        HttpServer.resetRoutesForTests();
        HttpServer.loadAnnotationRoutes();

        String response = HttpServer.getRoute("/greeting")
                .execute(new HttpRequest(new URI("/greeting")), new HttpResponse());

        assertEquals("Hola World", response);
    }

    @Test
    public void shouldResolveRequestParamFromQueryString() throws Exception {
        HttpServer.resetRoutesForTests();
        HttpServer.loadAnnotationRoutes();

        String response = HttpServer.getRoute("/greeting")
                .execute(new HttpRequest(new URI("/greeting?name=Ana%20Maria")), new HttpResponse());

        assertEquals("Hola Ana Maria", response);
    }

    @Test
    public void shouldLoadSingleControllerExplicitly() {
        AnnotationApplicationContext context = new AnnotationApplicationContext();

        context.load(HelloController.class);

        assertTrue(context.getRoutes().containsKey("/"));
        assertTrue(context.getRoutes().containsKey("/pi"));
    }
}