package com.adojos.app.http;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
        return queryParams.get(varname);
    }

    private Map<String, String> parseQueryParams(String rawQuery) {
        Map<String, String> params = new HashMap<>();

        if (rawQuery == null || rawQuery.isEmpty()) {
            return params;
        }

        String[] pairs = rawQuery.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                params.put(decode(keyValue[0]), decode(keyValue[1]));
            }
        }
        return params;
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}