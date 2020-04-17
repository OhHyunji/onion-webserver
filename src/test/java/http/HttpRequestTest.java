package http;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {

    @Test
    public void request_GET() throws IOException {
        InputStream in = getClass().getResourceAsStream("/httpGet.txt");
        HttpRequest request = new HttpRequest(in);

        assertEquals(HttpMethod.GET, request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection").orElse(null));
        assertEquals("javajigi", request.getRequiredParams("userId"));
    }

    @Test
    public void request_POST() throws IOException {
        InputStream in = getClass().getResourceAsStream("/httpPost.txt");
        HttpRequest request = new HttpRequest(in);

        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection").orElse(null));
        assertEquals("javajigi", request.getRequiredParams("userId"));
    }

    @Test
    public void request_POST2() throws Exception {
        InputStream in = getClass().getResourceAsStream("/httpPost2.txt");
        HttpRequest request = new HttpRequest(in);

        assertEquals(HttpMethod.POST, request.getMethod());
        assertEquals("/user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection").orElse(null));
        assertEquals("1", request.getRequiredParams("id"));
        assertEquals("javajigi", request.getRequiredParams("userId"));
    }
}