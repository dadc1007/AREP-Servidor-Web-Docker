package com.adojos.app.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class HttpRequestTest {

    @Test
    public void shouldExtractQueryValues() throws URISyntaxException {
        HttpRequest request = new HttpRequest(new URI("/hello?name=Pedro&city=Bogota"));

        assertEquals("Pedro", request.getValues("name"));
        assertEquals("Bogota", request.getValues("city"));
    }

    @Test
    public void shouldReturnNullWhenQueryParamDoesNotExist() throws URISyntaxException {
        HttpRequest request = new HttpRequest(new URI("/hello?name=Pedro"));

        assertNull(request.getValues("age"));
    }
}