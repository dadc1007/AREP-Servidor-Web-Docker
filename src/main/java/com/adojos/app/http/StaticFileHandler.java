package com.adojos.app.http;

import java.io.File;

public class StaticFileHandler {
    private static final String DEFAULT_STATIC_FOLDER = "target/classes";
    private static volatile String staticFolder = DEFAULT_STATIC_FOLDER;

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
        if (file.exists() && file.isFile()) {
            return file;
        }
        return null;
    }
}