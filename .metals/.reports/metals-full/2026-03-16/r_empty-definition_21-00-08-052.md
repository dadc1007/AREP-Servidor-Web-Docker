error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/App.java:_empty_/HttpServer#installShutdownHook#
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/App.java
empty definition using pc, found symbol in pc: _empty_/HttpServer#installShutdownHook#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 267
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/App.java
text:
```scala
package com.adojos.app;

import com.adojos.app.http.HttpServer;

public class App {
    public static void main(String[] args) throws Exception {
        HttpServer.staticfiles("/webroot/public");
        HttpServer.loadAnnotationRoutes();
        HttpServer.@@installShutdownHook();
        HttpServer.start();
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/HttpServer#installShutdownHook#