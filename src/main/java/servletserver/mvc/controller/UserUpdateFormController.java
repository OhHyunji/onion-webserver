package servletserver.mvc.controller;

import core.db.DataBase;
import core.model.User;
import servletserver.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserUpdateFormController implements Controller {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(allowUpdate(request)) {
            User user = DataBase.findByUserId(request.getParameter("userId")).orElseThrow(IllegalStateException::new);
            request.setAttribute("user", user);
            return "/user/updateForm.jsp";
        } else {
            throw new IllegalStateException();
        }
    }

    private boolean allowUpdate(HttpServletRequest req) {
        User loginUser = SessionUtils.getLoginUser(req).orElseThrow(IllegalStateException::new);
        return loginUser.getUserId().equals(req.getParameter("userId"));
    }
}
