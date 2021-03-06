package servletserver.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * HomeServlet
 * LoginFormServlet
 * UserUpdateFormServlet
 */
public class ForwardController implements Controller{
    private final String forwardUrl;

    public ForwardController(String forwardUrl) {
        if(Objects.isNull(forwardUrl)) {
            throw new NullPointerException("forwardUrl is null");
        }

        this.forwardUrl = forwardUrl;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return forwardUrl;
    }
}
