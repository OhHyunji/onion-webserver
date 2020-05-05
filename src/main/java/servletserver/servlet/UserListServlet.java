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
import java.util.Optional;

@WebServlet(WebServerPath.USER_LIST)
public class UserListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> loginUser = SessionUtils.getLoginUser(req);

        if(loginUser.isPresent()) {
            req.setAttribute("users", DataBase.findAll());
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/user/list.jsp");
            requestDispatcher.forward(req, resp);
        } else {
            resp.sendRedirect(WebServerPath.LOGIN_FORM);
        }
    }
}
