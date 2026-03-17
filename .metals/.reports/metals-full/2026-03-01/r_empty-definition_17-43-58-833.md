error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Microframeworks-WEB/src/main/java/com/adojos/app/examples/WebAppExample.java:_empty_/HttpServer#staticfiles#
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Microframeworks-WEB/src/main/java/com/adojos/app/examples/WebAppExample.java
empty definition using pc, found symbol in pc: _empty_/HttpServer#staticfiles#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 275
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Microframeworks-WEB/src/main/java/com/adojos/app/examples/WebAppExample.java
text:
```scala
package com.adojos.app.examples;

import java.io.IOException;
import java.net.URISyntaxException;

import com.adojos.app.HttpServer;

public class WebAppExample {
    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer.@@staticfiles("/webroot/public");

        HttpServer.get("/App/hello", (req, res) -> "Hello " + req.getValues("name"));
        HttpServer.get("/App/pi", (req, res) -> String.valueOf(Math.PI));
        HttpServer.get("/App/euler", (req, res) -> String.valueOf(Math.E));

        HttpServer.main(args);
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: _empty_/HttpServer#staticfiles#