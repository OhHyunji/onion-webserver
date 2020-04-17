package controller;

import http.HttpRequest;
import http.HttpResponse;

public class DefaultController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {

    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        response.forward(request.getUrl());
    }
}
