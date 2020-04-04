import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private static final String WEBAPP_ROOT_PATH = "./webapp";

	private Socket connection;

	public RequestHandler(Socket connection) {
		this.connection = connection;
	}

	public void run() {
		log.debug("New Client Connected. IP: {}, Port: {}", connection.getInetAddress(), connection.getPort());

		try(InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			/* request 읽기 (by InputStream) */
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

			String line = bufferedReader.readLine();
			if(line == null) {
				return;
			}

			// request line: GET /index.html HTTP/1.1
			log.debug("{}", line);
			String[] tokens = line.split(" ");
			String method = tokens[0];
			String url = tokens[1];

			// request header
			int contentLength = 0;
			while (!line.equals("")) {
				log.debug("header: {}", line);
				line = bufferedReader.readLine();
				if(line.contains("Content-Length")) {
					contentLength = getContentLength(line);
				}
			}

			/* response 만들기 (by OutputStream) */
			DataOutputStream dos = new DataOutputStream(out);

			if(method.equals("POST") && url.startsWith("/user/create")) {
				String body = IOUtils.readData(bufferedReader, contentLength);
				Map<String, String> params = HttpRequestUtils.parseQueryString(body);
				User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
				log.debug(user.toString());
				DataBase.addUser(user);

				response302Header(dos, "/index.html");
			} else if(method.equals("POST") && url.startsWith("/user/login")) {
				String body = IOUtils.readData(bufferedReader, contentLength);
				Map<String, String> params = HttpRequestUtils.parseQueryString(body);
				User user = DataBase.findByUserId(params.get("userId"));

				// 로그인 성공
				if(Objects.nonNull(user) && user.getPassword().equals(params.get("password"))) {
					response302HeaderWithCookie(dos, "/index.html", "login=true");
				}
				// 로그인 실패
				response302HeaderWithCookie(dos, "/user/login_failed.html", "login=false");
			} else {
				byte[] body = Files.readAllBytes(Paths.get(WEBAPP_ROOT_PATH + url));
				response200Header(dos, body.length);
				responseBody(dos,body);
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

	private int getContentLength(String line) {
		String[] headerTokens = line.split(":");
		return Integer.parseInt(headerTokens[1].trim());
	}
}
