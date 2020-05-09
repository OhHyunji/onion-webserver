package servletserver.mvc.controller;

import core.db.DataBase;
import core.model.User;
import core.route.WebServerPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class LoginController implements Controller {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Optional<User> loginUser = getLoginUser(request.getParameter("userId"), request.getParameter("password"));

        if(loginUser.isPresent()) {
            request.getSession().setAttribute("user", loginUser.get());
            response.addHeader("Set-Cookie", "login=true");
            return WebServerPath.REDIRECT_PREFIX + WebServerPath.HOME;
        } else {
            response.addHeader("Set-Cookie", "login=false");
            request.setAttribute("loginFailed", true);
            return WebServerPath.LOGIN;
        }
    }

    private Optional<User> getLoginUser(String userId, String password) {
        return DataBase.findByUserId(userId).filter(u -> u.getPassword().equals(password));
    }
}
