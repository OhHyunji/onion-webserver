package servletserver.servlet;

import webserver.db.DataBase;
import webserver.model.User;
import webserver.route.WebAppPath;
import webserver.route.WebServerPath;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(WebServerPath.LOGIN)
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> user = DataBase.findByUserId(req.getParameter("userId"));

//        if(user.isPresent() && password.equals(u.getPassword()) {
//            String password = req.getParameter("password");
//            if(password.equals(u.getPassword())) {
//                resp.addHeader("Set-Cookie", "login=true");
//                resp.sendRedirect(WebAppPath.DEFAULT_PAGE);
//            }
//        }

        resp.addHeader("Set-Cookie", "login=false");
        resp.sendRedirect(WebAppPath.LOGIN_FAILED_PAGE);

    }
}
