error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Microframeworks-WEB/src/main/java/com/adojos/app/HttpRequest.java:java/util/Map#get().
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Microframeworks-WEB/src/main/java/com/adojos/app/HttpRequest.java
empty definition using pc, found symbol in pc: java/util/Map#get().
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 448
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Microframeworks-WEB/src/main/java/com/adojos/app/HttpRequest.java
text:
```scala
package com.adojos.app;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private final Map<String, String> queryParams;

    public HttpRequest(URI uri) {
        this.queryParams = parseQueryParams(uri.getRawQuery());
    }

    public HttpRequest() {
        this.queryParams = new HashMap<>();
    }

    public String getValues(String varname) {
        return queryParams.@@get(varname);
    }

    private Map<String, String> parseQueryParams(String rawQuery) {
        Map<String, String> params = new HashMap<>();

        if (rawQuery == null || rawQuery.isEmpty()) {
            return params;
        }

        String[] pairs = rawQuery.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");

            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }

        }
        return params;
    }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: java/util/Map#get().