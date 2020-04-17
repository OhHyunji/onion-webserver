import db.DataBase;
import http.HttpMethod;
import http.HttpRequest;
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

		try(InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			HttpRequest request = new HttpRequest(in);
			HttpMethod method = request.getMethod();
			String url = request.getPath();

			/* response 만들기 (by OutputStream) */
			DataOutputStream dos = new DataOutputStream(out);

			if(HttpMethod.GET == method && url.equals(ROOT_REQUEST_MAPPING)) {
				response302Header(dos, DEFAULT_PAGE);
			} else if(HttpMethod.POST == method && url.equals(USER_CREATE_REQUEST_MAPPING)) {
				User user = new User(
						request.getRequiredParams("userId"),
						request.getRequiredParams("password"),
						request.getRequiredParams("name"),
						request.getRequiredParams("email"));

				log.debug(user.toString());
				DataBase.addUser(user);
				response302Header(dos, DEFAULT_PAGE);
			} else if(HttpMethod.POST == method && url.equals(USER_LOGIN_REQUEST_MAPPING)) {
				String userId = request.getRequiredParams("userId");
				User user = DataBase.findByUserId(userId);

				// 로그인 성공
				if(Objects.nonNull(user)) {
					String password = request.getRequiredParams("password");
					if(password.equals(user.getPassword())) {
						response302HeaderWithCookie(dos, DEFAULT_PAGE, "login=true");
					}
				}
				// 로그인 실패
				response302HeaderWithCookie(dos, LOGIN_FAILED_PAGE, "login=false");
			} else if(HttpMethod.GET == method && url.equals(USER_LIST_REQUEST_MAPPING)) {
				// 로그인 상태이면 사용자목록 출력
				boolean isLoginSuccess = request.getCookies("login").map(Boolean::parseBoolean).orElse(false);
				if(isLoginSuccess) {
					byte[] body = getUserListHtml().getBytes();
					response200Header(dos, body.length);
					responseBody(dos, body);
				}
				// 로그인 상태가 아니면 "/user/login.html"로 이동
				response302Header(dos, LOGIN_PAGE);
			} else {
				byte[] body = Files.readAllBytes(Paths.get(WEBAPP_ROOT_PATH + url));

				if(url.endsWith(".css")) {
					response200CssHeader(dos, body.length);
				} else {
					response200Header(dos, body.length);
				}

				responseBody(dos, body);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void response302Header(DataOutputStream dos, String location) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + location + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void response302HeaderWithCookie(DataOutputStream dos, String location, String cookie) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + location + "\r\n");
			dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
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
