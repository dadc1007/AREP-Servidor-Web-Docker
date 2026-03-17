package com.adojos.app.http;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class StaticFileHandlerTest {

    @Test
    public void shouldFindStaticFileWhenFolderHasLeadingSlash() throws IOException {
        createSampleStaticFile();

        StaticFileHandler.setStaticFolder("/test-static");

        File file = StaticFileHandler.getStaticFile("/sample.txt");
        assertNotNull(file);
    }

    @Test
    public void shouldFindStaticFileWhenFolderHasNoLeadingSlash() throws IOException {
        createSampleStaticFile();

        StaticFileHandler.setStaticFolder("test-static");

        File file = StaticFileHandler.getStaticFile("/sample.txt");
        assertNotNull(file);
    }

    @Test
    public void shouldReturnNullWhenFileDoesNotExist() {
        StaticFileHandler.setStaticFolder("/test-static");

        File file = StaticFileHandler.getStaticFile("/missing.txt");
        assertNull(file);
    }

    private void createSampleStaticFile() throws IOException {
        Path folder = Paths.get("target", "classes", "test-static");
        Files.createDirectories(folder);
        Files.writeString(folder.resolve("sample.txt"), "sample");
    }
}