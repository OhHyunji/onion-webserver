package webserver.controller;

import http.HttpRequest;
import http.HttpResponse;
import webserver.route.FrontPath;

public class RootController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {

    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        response.redirect(FrontPath.DEFAULT_PAGE);
    }
}
