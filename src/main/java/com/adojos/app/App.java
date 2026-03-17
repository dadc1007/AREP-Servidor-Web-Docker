package com.adojos.app;

import com.adojos.app.http.HttpServer;

public class App {
    public static void main(String[] args) throws Exception {
        HttpServer.staticfiles("/webroot/public");
        HttpServer.loadAnnotationRoutes();
        HttpServer.start();
    }
}
