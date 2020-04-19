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
        Optional<User> user = DataBase.findByUserId(request.getRequiredParams("userId"));

        user.ifPresent(u -> {
            String password = request.getRequiredParams("password");
            if(password.equals(u.getPassword())) {
                response.addHeader("Set-Cookie", "login=true");
                response.redirect(WebAppPath.DEFAULT_PAGE);
            }
        });

        // 로그인 실패
        response.addHeader("Set-Cookie", "login=false");
        response.redirect(WebAppPath.LOGIN_FAILED_PAGE);
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {

    }
}
