error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/test/java/com/adojos/app/http/HttpServerConcurrencyTest.java:java/util/concurrent/ExecutorService#shutdown().
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/test/java/com/adojos/app/http/HttpServerConcurrencyTest.java
empty definition using pc, found symbol in pc: java/util/concurrent/ExecutorService#shutdown().
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1119
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/test/java/com/adojos/app/http/HttpServerConcurrencyTest.java
text:
```scala
package com.adojos.app.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Test;

public class HttpServerConcurrencyTest {

    @After
    public void tearDown() {
        HttpServer.resetRoutesForTests();
    }

    @Test
    public void shouldLoadAnnotatedRoutesSafelyWhenCalledConcurrently()
            throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            tasks.add(() -> {
                HttpServer.loadAnnotationRoutes();
                return null;
            });
        }

        List<Future<Void>> futures = executor.invokeAll(tasks);
        executor.@@shutdown();

        for (Future<Void> future : futures) {
            future.get();
        }

        assertTrue(HttpServer.hasRoute("/"));
        assertTrue(HttpServer.hasRoute("/greeting"));
    }

    @Test
    public void shouldExposeAWorkerPoolLargerThanOneThread() {
        assertTrue(HttpServer.getWorkerThreadCount() > 1);
        assertEquals(Math.max(4, Runtime.getRuntime().availableProcessors() * 2), HttpServer.getWorkerThreadCount());
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: java/util/concurrent/ExecutorService#shutdown().