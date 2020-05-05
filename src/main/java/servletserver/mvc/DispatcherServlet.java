package servletserver.mvc;

import core.route.WebServerPath;

import javax.servlet.annotation.WebServlet;

@WebServlet(name="dispatcher", urlPatterns = WebServerPath.ROOT, loadOnStartup = 1)
public class DispatcherServlet {

}
