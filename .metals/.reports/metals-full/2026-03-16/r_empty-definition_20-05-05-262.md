error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/App.java:_empty_/HttpServer#start#
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/App.java
empty definition using pc, found symbol in pc: _empty_/HttpServer#start#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 316
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/App.java
text:
```scala
package com.adojos.app;

import com.adojos.app.context.AnnotationApplicationContext;
import com.adojos.app.http.HttpServer;

public class App {
    public static void main(String[] args) throws Exception {
        HttpServer.staticfiles("/webroot/public");
        configureRoutes(args);
        HttpServer.@@start();
    }

    static void configureRoutes(String[] args) throws ClassNotFoundException {
        if (args == null || args.length == 0) {
            HttpServer.loadAnnotationRoutes();
            return;
        }

        AnnotationApplicationContext context = new AnnotationApplicationContext();
        for (String className : args) {
            Class<?> controllerClass = Class.forName(className);
            context.load(controllerClass);
        }
        HttpServer.registerRoutes(context.getRoutes());
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/HttpServer#start#