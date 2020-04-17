package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import route.FrontPath;

import java.util.Optional;

public class LoginController extends AbstractController {
    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        Optional<User> user = findUser(request.getRequiredParams("userId"));
        user.ifPresent(u -> {
            String password = request.getRequiredParams("password");
            if(password.equals(u.getPassword())) {
                response.addHeader("Set-Cookie", "login=true");
                response.redirect(FrontPath.DEFAULT_PAGE);
            }
        });

        // 로그인 실패
        response.addHeader("Set-Cookie", "login=false");
        response.redirect(FrontPath.LOGIN_FAILED_PAGE);
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {

    }

    private Optional<User> findUser(String userId) {
        return Optional.ofNullable(DataBase.findByUserId(userId));
    }
}
