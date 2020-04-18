package servletserver.servlet;

import servletserver.db.DataBase;
import servletserver.route.WebServletPath;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(WebServletPath.USER_LIST)
public class UserListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("users", DataBase.findAll());
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/user/list.jsp");
        requestDispatcher.forward(req, resp);
    }
}