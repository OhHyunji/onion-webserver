package servletserver.utils;

import core.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionUtils {

    public static Optional<User> getLoginUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return Optional.ofNullable(session.getAttribute("user")).map(u -> (User)u);
    }
}
