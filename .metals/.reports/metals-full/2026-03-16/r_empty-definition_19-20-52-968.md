error id: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/StaticFileHandler.java:java/io/File#exists().
file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/StaticFileHandler.java
empty definition using pc, found symbol in pc: java/io/File#exists().
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 750
uri: file:///C:/Users/dadic/Documentos/Universidad/Semestre%209/AREP/Corte%202/AREP-Servidor-Web-Docker/src/main/java/com/adojos/app/StaticFileHandler.java
text:
```scala
package com.adojos.app;

import java.io.File;

public class StaticFileHandler {
    private static final String DEFAULT_STATIC_FOLDER = "target/classes";
    private static String staticFolder = DEFAULT_STATIC_FOLDER;

    public static void setStaticFolder(String folder) {
        if (folder == null || folder.trim().isEmpty()) {
            staticFolder = DEFAULT_STATIC_FOLDER;
            return;
        }

        String normalizedFolder = folder.startsWith("/") ? folder : "/" + folder;
        staticFolder = DEFAULT_STATIC_FOLDER + normalizedFolder;
    }

    public static File getStaticFile(String path) {
        String fullPath = staticFolder + path;
        File file = new File(fullPath);

        if (file.exis@@ts() && file.isFile()) {
            return file;
        }

        return null;
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: java/io/File#exists().