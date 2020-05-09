package servletserver.servlet;

import core.db.DataBase;
import core.model.User;
import core.route.WebAppPath;
import core.route.WebServerPath;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

// @WebServlet(WebServerPath.LOGIN)
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<User> loginUser = getLoginUser(req.getParameter("userId"), req.getParameter("password"));

        if(loginUser.isPresent()) {
            req.getSession().setAttribute("user", loginUser.get());
            resp.addHeader("Set-Cookie", "login=true");
            resp.sendRedirect(WebServerPath.HOME);
        } else {
            resp.addHeader("Set-Cookie", "login=false");
            req.setAttribute("loginFailed", true);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/user/login.jsp");
            requestDispatcher.forward(req, resp);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------------------------
    // -- private
    // ---------------------------------------------------------------------------------------------------------------------------------------------------------

    private Optional<User> getLoginUser(String userId, String password) {
        return DataBase.findByUserId(userId).filter(u -> u.getPassword().equals(password));
    }
}
