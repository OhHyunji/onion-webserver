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
            return "redirect:/" + WebServerPath.HOME;
        } else {
            response.addHeader("Set-Cookie", "login=false");
            request.setAttribute("loginFailed", true);
            // TODO 이렇게 보내면 request, response 는 어떻게 넘어가는거지
            return "/user/login.jsp";
        }
    }

    private Optional<User> getLoginUser(String userId, String password) {
        return DataBase.findByUserId(userId).filter(u -> u.getPassword().equals(password));
    }
}
