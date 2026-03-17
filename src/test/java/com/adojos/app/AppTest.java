package com.adojos.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.adojos.app.http.HttpServer;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void shouldLoadAllAnnotatedControllersForServerMode() throws Exception {
        HttpServer.resetRoutesForTests();

        HttpServer.loadAnnotationRoutes();

        assertTrue(HttpServer.hasRoute("/pi"));
        assertTrue(HttpServer.hasRoute("/greeting"));
    }

    @Test
    public void shouldExecuteSpecifiedControllerFromCommandLineMode() throws Exception {
        String response = CommandLineApp.execute("com.adojos.app.controllers.HelloController", "/pi");

        assertEquals("PI = " + Math.PI, response);
    }

    @Test
    public void shouldResolveRequestParamsInCommandLineMode() throws Exception {
        String response = CommandLineApp.execute(
                "com.adojos.app.controllers.GreetingController",
                "/greeting?name=Daniel");

        assertEquals("Hola Daniel", response);
    }
}
