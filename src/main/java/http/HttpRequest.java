package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {
	private final HttpMethod method;
	private final String url;

	private final Map<String, String> header = Maps.newHashMap();
	private final Map<String, String> params = Maps.newHashMap();

	public HttpRequest(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

		// request line: GET /index.html HTTP/1.1
		String line = bufferedReader.readLine();
		String[] tokens = line.split(" ");
		this.method = HttpMethod.valueOf(tokens[0]);

		boolean hasQueryParam = tokens[1].contains("?");
		if(!hasQueryParam) {
			this.url = tokens[1];
		} else {
			String[] urlTokens = tokens[1].split("\\?");
			this.url = urlTokens[0];
			this.params.putAll(HttpRequestUtils.parseQueryString(urlTokens[1]));
		}

		// request header
		while (!line.equals("")) {
			line = bufferedReader.readLine();
			setHeader(line);
		}

		// bodyParams
		if(HttpMethod.POST == this.method) {
			Integer contentLength = getHeader("Content-Length").map(Integer::valueOf).orElseThrow(IllegalArgumentException::new);
			String body = IOUtils.readData(bufferedReader, contentLength);
			this.params.putAll(HttpRequestUtils.parseQueryString(body));
		}
	}

	public HttpMethod getMethod() {
		return this.method;
	}

	public String getUrl() {
		return this.url;
	}

	public Optional<String> getHeader(String key) {
		return Optional.ofNullable(this.header.get(key));
	}

	public Optional<String> getParams(String key) {
		return Optional.ofNullable(this.params.get(key));
	}

	public String getRequiredParams(String key) {
		return Optional.ofNullable(this.params.get(key)).orElseThrow(IllegalArgumentException::new);
	}

	public Optional<String> getCookies(String key) {
		Map<String, String> cookies = getHeader("Cookie").map(HttpRequestUtils::parseCookies).orElse(Maps.newHashMap());
		return Optional.ofNullable(cookies.get(key));
	}

	private void setHeader(String line) {
		if(Strings.isNullOrEmpty(line)) return;

		String[] tokens = line.split(":");
		this.header.put(tokens[0].trim(), tokens[1].trim());
	}
}
