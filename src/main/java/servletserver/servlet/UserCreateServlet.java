package servletserver.servlet;

import webserver.db.DataBase;
import webserver.model.User;
import webserver.route.WebServerPath;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(WebServerPath.USER_CREATE)
public class UserCreateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"), req.getParameter("email"));
        DataBase.addUser(user);

        resp.sendRedirect(WebServerPath.USER_LIST);
    }
}
