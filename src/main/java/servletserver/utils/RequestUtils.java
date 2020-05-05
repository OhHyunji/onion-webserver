package servletserver.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class RequestUtils {

    public static Optional<Object> getLoginUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return Optional.ofNullable(session.getAttribute("user"));
    }
}
