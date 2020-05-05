package servletserver.controller;

import core.db.DataBase;
import core.model.User;
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
            return "/user/list.jsp";
        } else {
            return "redirect:/users/loginForm";
        }
    }
}
