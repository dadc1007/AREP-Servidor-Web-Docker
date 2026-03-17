package com.adojos.app.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import com.adojos.app.context.AnnotationApplicationContext;

public class HttpServer {
    private static final Map<String, WebMethod> ENDPOINTS = new HashMap<>();
    private static boolean annotationRoutesLoaded = false;

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadAnnotationRoutes();
        start();
    }

    public static void start() throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8080.");
            System.exit(1);
        }
        Socket clientSocket = null;

        boolean running = true;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine;
            String outputLine;
            boolean firstLine = true;
            String reqPath = "";
            HttpRequest request = null;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);
                if (firstLine) {
                    String[] flTokens = inputLine.split(" ");
                    String struri = flTokens[1];
                    URI requri = new URI(struri);
                    reqPath = requri.getPath();
                    request = new HttpRequest(requri);
                    System.out.println("Path: " + reqPath);
                    firstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            WebMethod method = ENDPOINTS.get(reqPath);
            HttpResponse response = new HttpResponse();

            if (method != null) {
                outputLine = "HTTP/1.1 200 OK\n\r"
                        + "Content-Type: text/html\n\r"
                        + "\n\r"
                        + "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "<meta charset=\"UTF-8\">"
                        + "<title>PI</title>\n"
                        + "</head>"
                        + "<body>"
                        + method.execute(request, response)
                        + "</body>"
                        + "</html>";
                out.println(outputLine);
            } else {
                File staticFile = StaticFileHandler.getStaticFile(reqPath);
                if (staticFile != null) {
                    String fileBody = Files.readString(staticFile.toPath());
                    outputLine = "HTTP/1.1 200 OK\n\r"
                            + "Content-Type: " + getContentType(staticFile) + "\n\r"
                            + "\n\r"
                            + fileBody;
                } else {
                    outputLine = "HTTP/1.1 404 Not Found\n\r"
                            + "Content-Type: text/plain\n\r"
                            + "\n\r"
                            + "Resource not found";
                }
                out.println(outputLine);
            }

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static void get(String path, WebMethod method) {
        ENDPOINTS.put(path, method);
    }

    public static void registerRoutes(Map<String, WebMethod> routes) {
        for (Map.Entry<String, WebMethod> route : routes.entrySet()) {
            ENDPOINTS.putIfAbsent(route.getKey(), route.getValue());
        }
    }

    public static void loadAnnotationRoutes() {
        if (annotationRoutesLoaded) {
            return;
        }

        AnnotationApplicationContext context = new AnnotationApplicationContext();
        context.load();
        registerRoutes(context.getRoutes());
        annotationRoutesLoaded = true;
    }

    public static void resetRoutesForTests() {
        ENDPOINTS.clear();
        annotationRoutesLoaded = false;
    }

    public static boolean hasRoute(String path) {
        return ENDPOINTS.containsKey(path);
    }

    public static WebMethod getRoute(String path) {
        return ENDPOINTS.get(path);
    }

    public static void staticfiles(String folder) {
        StaticFileHandler.setStaticFolder(folder);
    }

    private static String getContentType(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        }
        if (fileName.endsWith(".css")) {
            return "text/css";
        }
        if (fileName.endsWith(".js")) {
            return "application/javascript";
        }
        if (fileName.endsWith(".json")) {
            return "application/json";
        }
        if (fileName.endsWith(".png")) {
            return "image/png";
        }
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        return "text/plain";
    }
}