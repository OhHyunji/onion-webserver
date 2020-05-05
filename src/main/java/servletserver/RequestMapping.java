package servletserver;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servletserver.controller.*;

import java.util.Map;

public class RequestMapping {
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private Map<String, Controller> mappings = Maps.newHashMap();

    void initMapping() {
        mappings.put("/", new ForwardController("/home.jsp"));
        mappings.put("/users/form", new ForwardController("/user/form.jsp"));
        mappings.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        mappings.put("/users", new UserListController());
        mappings.put("/users/login", new LoginController());
        mappings.put("/users/profile", new ProfileController());
        mappings.put("/users/logout", new LogoutController());
        mappings.put("/users/create", new UserCreateController());
        mappings.put("/users/updateForm", new UserUpdateFormController());
        mappings.put("/users/update", new UserUpdateController());

        log.info("###### Initialized Request Mapping ######");
    }

    public Controller findController(String url) {
        return mappings.get(url);
    }

    void put(String url, Controller controller) {
        mappings.put(url, controller);
    }
}
