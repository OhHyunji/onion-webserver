package webserver.controller;

import webserver.db.DataBase;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.model.User;
import webserver.route.WebAppPath;

import java.util.Optional;

public class LoginController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        Optional<User> loginUser = DataBase.findByUserId(request.getRequiredParams("userId"))
                .filter(u -> u.getPassword().equals(request.getRequiredParams("password")));

        if(loginUser.isPresent()) {
            response.addHeader("Set-Cookie", "login=true");
            response.redirect(WebAppPath.DEFAULT_PAGE);
        } else {
            response.addHeader("Set-Cookie", "login=false");
            response.redirect(WebAppPath.LOGIN_FAILED_PAGE);
        }
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {

    }
}
