package http;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private static final String WEBAPP_ROOT_PATH = "./webapp";

    private final DataOutputStream dos;
    private final Map<String, String> header = Maps.newHashMap();

    public HttpResponse(OutputStream outputStream) {
        dos = new DataOutputStream(outputStream);
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public void forward(String url) {
        try {
            byte[] body = Files.readAllBytes(Paths.get(WEBAPP_ROOT_PATH + url));

            if(url.endsWith(".css")) {
                header.put("Content-Type", "text/css");
            } else if(url.endsWith(".js")) {
                header.put("Content-Type", "application/javascript");
            } else {
                header.put("Content-Type", "text/html;charset=utf-8");
            }
            header.put("Content-Length", String.valueOf(body.length));

            response(HttpResponseStatus.OK, header, body);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void forwardBody(String bodyStr) {
        byte[] body = bodyStr.getBytes();

        header.put("Content-Type", "text/html;charset=utf-8");
        header.put("Content-Length", String.valueOf(body.length));

        response(HttpResponseStatus.OK, header, body);
    }

    public void redirect(String redirectUrl) {
        header.put("Location", redirectUrl);
        response(HttpResponseStatus.FOUND, header);
    }

    private void response(HttpResponseStatus status, Map<String, String> header) {
        try {
            dos.writeBytes(getResponseLine(status) + "\r\n");
            writeHeader(header);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void response(HttpResponseStatus status, Map<String, String> header, byte[] body) {
        try {
            dos.writeBytes(getResponseLine(status) + "\r\n");
            writeHeader(header);
            dos.writeBytes("\r\n");
            writeBody(body);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void writeHeader(Map<String, String> header) {
        mapToStringLines(header, ":").forEach(l -> {
            try {
                dos.writeBytes(l + "\r\n");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private void writeBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<String> mapToStringLines(Map<String, String> map, String delimiter) {
        List<String> result = Lists.newArrayList();
        for(String key: map.keySet()) {
            String str = String.format("%s%s %s", key, delimiter, map.get(key));
            result.add(str);
        }
        return result;
    }

    private String getResponseLine(HttpResponseStatus status) {
        return String.format("HTTP/1.1 %d %s", status.getCode(), status.getMessage());
    }
}
