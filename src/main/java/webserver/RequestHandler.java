package webserver;

import com.google.common.collect.Maps;
import webserver.controller.*;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.route.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        log.debug("New Client Connected. IP: {}, Port: {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            Map<String, Controller> controllerMap = Maps.newHashMap();
            controllerMap.put(RequestMapping.ROOT, new RootController());
            controllerMap.put(RequestMapping.USER_CREATE, new UserCreateController());
            controllerMap.put(RequestMapping.USER_LIST, new UserListController());
            controllerMap.put(RequestMapping.LOGIN, new LoginController());

            Optional.ofNullable(controllerMap.get(request.getUrl()))
                    .orElse(new DefaultController())
                    .service(request, response);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
