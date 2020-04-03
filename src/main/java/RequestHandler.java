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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private static final String WEBAPP_ROOT_PATH = "./webapp";
	private static final String ROOT_PATH = "/";
	private static final String DEFAULT_PATH = "/index.html";

	private Socket connection;

	public RequestHandler(Socket connection) {
		this.connection = connection;
	}

	public void run() {
		log.debug("New Client Connected. IP: {}, Port: {}", connection.getInetAddress(), connection.getPort());

		try(InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

			String filePath = DEFAULT_PATH;

			while (true) {
				String line = bufferedReader.readLine();

				if(Strings.isNullOrEmpty(line)) {
					break;
				}

				/* startLine: GET /index.html HTTP/1.1 */
				if(line.contains("GET")) {
					String[] token = line.split(" ");
					String path = token[1];
					filePath = path.equals(ROOT_PATH) ? DEFAULT_PATH : path;
				}
			}

			byte[] body = Files.readAllBytes(Paths.get(WEBAPP_ROOT_PATH + filePath));

			DataOutputStream dos = new DataOutputStream(out);
			response200Header(dos, body.length);
			responseBody(dos,body);
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

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}

	}
}
