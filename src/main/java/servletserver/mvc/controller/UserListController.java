package servletserver.mvc.controller;

import core.db.DataBase;
import core.model.User;
import core.route.WebServerPath;
import servletserver.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class UserListController implements Controller{

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<User> loginUser = SessionUtils.getLoginUser(request);

        if(loginUser.isPresent()) {
            request.setAttribute("users", DataBase.findAll());
            return WebServerPath.USER_LIST;
        } else {
            return WebServerPath.REDIRECT_PREFIX + WebServerPath.LOGIN_FORM;
        }
    }
}
