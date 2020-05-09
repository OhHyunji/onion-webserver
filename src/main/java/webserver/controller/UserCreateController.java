package webserver.controller;

import core.db.DataBase;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import core.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.route.WebAppPath;

public class UserCreateController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(UserCreateController.class);

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        createUser(request.getRequiredParams("userId"), request.getRequiredParams("password"), request.getRequiredParams("name"), request.getRequiredParams("email"));
        response.redirect(WebAppPath.DEFAULT_PAGE);
    }

    @Override
    protected void doGet(HttpRequest request, HttpResponse response) {

    }

    private void createUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        log.debug(user.toString());
        DataBase.addUser(user);
    }
}
