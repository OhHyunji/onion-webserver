package servlet;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResource;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.File;

public class WebServerLauncher {
    private static final Logger log = LoggerFactory.getLogger(WebServerLauncher.class);

    public static void main(String[] args) throws ServletException, LifecycleException {
        String webappDirLocation = "webapp/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        /*
         * embedded tomcat 을 활용해 웹서버를 시작하는 경우 servlet 기반으로 작성한 매핑이 동작하지 않는다.
         * maven, gradle 같은 빌드도구를 사용할 경우 컴파일되는 output 경로가 WEB-INF/classes 가 아니기 때문이다.
         * 따라서 Tomcat 8.0의 경우 추가 설정이 필요하다.
         * 참고: https://www.slipp.net/questions/302
         *
         * what is /WEB-INF/ ?
         * 참고: https://stackoverflow.com/questions/19786142/what-is-web-inf-used-for-in-a-java-ee-web-application
         */

        Context context = tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        File additionWebInfClasses = new File("build/classes");
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);

        log.info("configuring app with baseDir: {}", new File(String.format("./%s", webappDirLocation)).getAbsoluteFile());

        tomcat.start();
        tomcat.getServer().await();
    }
}
