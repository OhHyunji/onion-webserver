package servletserver.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
    /**
     * redirect:
     *  return "redirect: ~"
     *
     * forward:
     *  return "jsp path"
     */
    String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
