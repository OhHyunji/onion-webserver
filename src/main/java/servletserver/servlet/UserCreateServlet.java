package servletserver.servlet;

import servletserver.db.DataBase;
import servletserver.model.User;
import servletserver.route.WebServletPath;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(WebServletPath.USER_CREATE)
public class UserCreateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"), req.getParameter("email"));
        DataBase.addUser(user);

        resp.sendRedirect(WebServletPath.USER_LIST);
    }
}
