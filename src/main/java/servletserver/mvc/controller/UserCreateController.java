package servletserver.mvc.controller;

import core.db.DataBase;
import core.model.User;
import core.route.WebServerPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserCreateController implements Controller {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
        DataBase.addUser(user);

        return WebServerPath.REDIRECT_PREFIX + WebServerPath.USER_LIST;
    }
}
