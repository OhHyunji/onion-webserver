package http;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class HttpResponseTest {
    @Test
    public void responseForward() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("httpForward.txt"));
        response.forward("/index.html");
    }

    @Test
    public void responseRedirect() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("httpRedirect.txt"));
        response.redirect("/index.html");
    }

    @Test
    public void responseCookies() throws Exception {
        HttpResponse response = new HttpResponse(createOutputStream("httpCookie.txt"));
        response.addHeader("Set-Cookie", "login=true");
        response.redirect("/index.html");
    }

    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File("src/" + filename));
    }
}