package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class DefaultController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {

    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        response.forward(request.getUrl());
    }
}
