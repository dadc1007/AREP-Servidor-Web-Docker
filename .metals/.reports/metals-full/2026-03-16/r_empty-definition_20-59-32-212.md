error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/http/HttpServer.java:java/util/concurrent/ExecutorService#shutdown().
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/http/HttpServer.java
empty definition using pc, found symbol in pc: java/util/concurrent/ExecutorService#shutdown().
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 10112
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/http/HttpServer.java
text:
```scala
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.adojos.app.context.AnnotationApplicationContext;

public class HttpServer {
    private static final int DEFAULT_WORKER_THREADS = Math.max(4, Runtime.getRuntime().availableProcessors() * 2);
    private static final int SHUTDOWN_TIMEOUT_SECONDS = 5;
    private static final Map<String, WebMethod> ENDPOINTS = new ConcurrentHashMap<>();
    private static final Object ROUTE_LOADING_LOCK = new Object();
    private static final Object LIFECYCLE_LOCK = new Object();
    private static final AtomicBoolean SHUTDOWN_HOOK_REGISTERED = new AtomicBoolean(false);

    private static volatile ServerSocket activeServerSocket;
    private static volatile boolean running;
    private static ExecutorService clientExecutor;
    private static boolean annotationRoutesLoaded = false;

    public static void main(String[] args) throws IOException, URISyntaxException {
        loadAnnotationRoutes();
        installShutdownHook();
        start();
    }

    public static void start() throws IOException, URISyntaxException {
        synchronized (LIFECYCLE_LOCK) {
            if (running) {
                throw new IllegalStateException("Server is already running.");
            }
            clientExecutor = createExecutor();
            running = true;
        }

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            activeServerSocket = serverSocket;
            System.out.println("Servidor concurrente escuchando en el puerto 8080 con "
                    + DEFAULT_WORKER_THREADS + " workers.");

            while (running) {
                try {
                    System.out.println("Listo para recibir ...");
                    Socket clientSocket = serverSocket.accept();
                    ExecutorService executor = clientExecutor;
                    if (executor == null || executor.isShutdown()) {
                        clientSocket.close();
                        continue;
                    }
                    executor.submit(() -> handleClient(clientSocket));
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Accept failed.");
                    }
                } catch (RejectedExecutionException e) {
                    System.err.println("Worker pool is shutting down. Request rejected.");
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8080.");
            System.exit(1);
        } finally {
            activeServerSocket = null;
            running = false;
            shutdownExecutorGracefully();
        }
    }

    public static void stop() {
        running = false;

        ServerSocket socketToClose = activeServerSocket;
        if (socketToClose != null && !socketToClose.isClosed()) {
            try {
                socketToClose.close();
            } catch (IOException e) {
                System.err.println("Failed to close server socket cleanly: " + e.getMessage());
            }
        }

        shutdownExecutorGracefully();
    }

    public static void installShutdownHook() {
        if (SHUTDOWN_HOOK_REGISTERED.compareAndSet(false, true)) {
            Runtime.getRuntime().addShutdownHook(new Thread(HttpServer::stop, "http-shutdown-hook"));
        }
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

        synchronized (ROUTE_LOADING_LOCK) {
            if (annotationRoutesLoaded) {
                return;
            }

            AnnotationApplicationContext context = new AnnotationApplicationContext();
            context.load();
            registerRoutes(context.getRoutes());
            annotationRoutesLoaded = true;
        }
    }

    public static void resetRoutesForTests() {
        ENDPOINTS.clear();
        annotationRoutesLoaded = false;
    }

    public static boolean hasRoute(String path) {
        return ENDPOINTS.containsKey(path);
    }

    public static int getWorkerThreadCount() {
        return DEFAULT_WORKER_THREADS;
    }

    public static boolean isRunning() {
        return running;
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

    private static void handleClient(Socket clientSocket) {
        try (Socket socket = clientSocket;
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            RequestContext requestContext = readRequest(in);
            String outputLine = buildResponse(requestContext.path(), requestContext.request());
            out.println(outputLine);
        } catch (IOException | URISyntaxException e) {
            System.err.println("Request handling failed: " + e.getMessage());
        }
    }

    private static RequestContext readRequest(BufferedReader in) throws IOException, URISyntaxException {
        String inputLine;
        boolean firstLine = true;
        String reqPath = "";
        HttpRequest request = new HttpRequest();

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

        return new RequestContext(reqPath, request);
    }

    private static String buildResponse(String reqPath, HttpRequest request) throws IOException {
        WebMethod method = ENDPOINTS.get(reqPath);
        HttpResponse response = new HttpResponse();

        if (method != null) {
            return "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
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
        }

        File staticFile = StaticFileHandler.getStaticFile(reqPath);
        if (staticFile != null) {
            String fileBody = Files.readString(staticFile.toPath());
            return "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: " + getContentType(staticFile) + "\r\n"
                    + "\r\n"
                    + fileBody;
        }

        return "HTTP/1.1 404 Not Found\r\n"
                + "Content-Type: text/plain\r\n"
                + "\r\n"
                + "Resource not found";
    }

    private record RequestContext(String path, HttpRequest request) {
    }

    private static final class ServerWorkerThreadFactory implements ThreadFactory {
        private final AtomicInteger workerCounter = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "http-worker-" + workerCounter.getAndIncrement());
            return thread;
        }
    }

    private static ExecutorService createExecutor() {
        return Executors.newFixedThreadPool(DEFAULT_WORKER_THREADS, new ServerWorkerThreadFactory());
    }

    private static void shutdownExecutorGracefully() {
        ExecutorService executor = clientExecutor;
        clientExecutor = null;

        if (executor == null) {
            return;
        }

        executor.@@shutdown();
        try {
            if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: java/util/concurrent/ExecutorService#shutdown().