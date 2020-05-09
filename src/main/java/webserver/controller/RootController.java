package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import core.route.WebAppPath;

public class RootController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {

    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {
        response.redirect(WebAppPath.DEFAULT_PAGE);
    }
}
