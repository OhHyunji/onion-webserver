package controller;

import http.HttpRequest;
import http.HttpResponse;

public abstract class AbstractController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        switch (request.getMethod()) {
            case GET:
                doGet(request, response);
            case POST:
                doPost(request, response);
            default:
                throw new UnsupportedOperationException();
        }
    }

    abstract protected void doPost(HttpRequest request, HttpResponse response);
    abstract protected void doGet(HttpRequest request, HttpResponse response);
}
