package servletserver.servlet;

import core.db.DataBase;
import core.model.User;
import core.route.WebServerPath;
import servletserver.utils.SessionUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// @WebServlet(WebServerPath.USER_UPDATE_FORM)
public class UserUpdateFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(allowUpdate(req)) {
            User user = DataBase.findByUserId(req.getParameter("userId")).orElseThrow(IllegalStateException::new);
            req.setAttribute("user", user);

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/user/updateForm.jsp");
            requestDispatcher.forward(req, resp);
        } else {
            throw new IllegalStateException();
        }
    }

    private boolean allowUpdate(HttpServletRequest req) {
        User loginUser = SessionUtils.getLoginUser(req).orElseThrow(IllegalStateException::new);
        return loginUser.getUserId().equals(req.getParameter("userId"));
    }
}
