import db.DataBase;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String WEBAPP_ROOT_PATH = "./webapp";

    private static final String DEFAULT_PAGE = "/index.html";
    private static final String LOGIN_PAGE = "/user/login.html";
    private static final String LOGIN_FAILED_PAGE = "/user/login_failed.html";

    private static final String ROOT_REQUEST_MAPPING = "/";
    private static final String USER_CREATE_REQUEST_MAPPING = "/user/create";
    private static final String USER_LOGIN_REQUEST_MAPPING = "/user/login";
    private static final String USER_LIST_REQUEST_MAPPING = "/user/list";

    private final Socket connection;

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        log.debug("New Client Connected. IP: {}, Port: {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpMethod method = request.getMethod();
            String url = request.getPath();

            HttpResponse response = new HttpResponse(out);

            if(HttpMethod.GET == method && url.equals(ROOT_REQUEST_MAPPING)) {
                response.redirect(DEFAULT_PAGE);
            } else if(HttpMethod.POST == method && url.equals(USER_CREATE_REQUEST_MAPPING)) {
                createUser(request.getRequiredParams("userId"), request.getRequiredParams("password"), request.getRequiredParams("name"), request.getRequiredParams("email"));
                response.redirect(DEFAULT_PAGE);
            } else if(HttpMethod.POST == method && url.equals(USER_LOGIN_REQUEST_MAPPING)) {
                // 로그인 성공
                Optional<User> user = findUser(request.getRequiredParams("userId"));
                user.ifPresent(u -> {
                    String password = request.getRequiredParams("password");
                    if(password.equals(u.getPassword())) {
                        response.addHeader("Set-Cookie", "login=true");
                        response.redirect(DEFAULT_PAGE);
                    }
                });
                // 로그인 실패
                response.addHeader("Set-Cookie", "login=false");
                response.redirect(LOGIN_FAILED_PAGE);
            } else if(HttpMethod.GET == method && url.equals(USER_LIST_REQUEST_MAPPING)) {
                // 로그인 상태이면 사용자목록 출력
                boolean isLoginSuccess = request.getCookies("login").map(Boolean::parseBoolean).orElse(false);
                if(isLoginSuccess) {
                    response.forwardBody(getUserListHtml());
                }
                // 로그인 상태가 아니면 로그인 페이지로 이동
                response.redirect(LOGIN_PAGE);
            } else {
                response.forward(url);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void createUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        log.debug(user.toString());
        DataBase.addUser(user);
    }

    private Optional<User> findUser(String userId) {
        return Optional.ofNullable(DataBase.findByUserId(userId));
    }

    private String getUserListHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table><tr><th>userId</th><th>userName</th><th>email</th></tr>");
        DataBase.findAll().forEach(u -> sb.append(getUserHtml(u)));
        sb.append("</table>");
        return sb.toString();
    }

    private String getUserHtml(User user) {
        return String.format("<tr><td>%s</td><td>%s</td><td>%s</td></tr>", user.getUserId(), user.getName(), user.getEmail());
    }
}
