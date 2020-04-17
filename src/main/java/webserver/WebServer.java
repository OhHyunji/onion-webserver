package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
	private static final Logger log = LoggerFactory.getLogger(WebServer.class);
	private static final int DEFAULT_PORT = 8080;

	public static void main(String[] args) throws Exception {
		int port = 0;

		if(Objects.isNull(args) || args.length == 0) {
			port = DEFAULT_PORT;
		} else {
			port = Integer.parseInt(args[0]);
		}

		// 서버 소켓 생성
		try(ServerSocket listenSocket = new ServerSocket(port)) {
			log.info("Web Application Server started {} port.", port);

			// 클라이언트가 연결될때까지 대기
			Socket connection;
			while (Objects.nonNull(connection = listenSocket.accept())) {
				RequestHandler requestHandler = new RequestHandler(connection);
				requestHandler.start();
			}
		}
	}
}
