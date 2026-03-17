package com.adojos.app.http;

public interface WebMethod {
    String execute(HttpRequest req, HttpResponse res);
}