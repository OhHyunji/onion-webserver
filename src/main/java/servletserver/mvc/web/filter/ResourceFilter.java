package servletserver.mvc.web.filter;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

// TODO search: servletFilter, servlet lifeCycle, embeddedTomcat

@WebFilter("/*")
public class ResourceFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(ResourceFilter.class);
    private static final List<String> resourcePrefixes = Lists.newArrayList();

    static {
        resourcePrefixes.add("/css");
        resourcePrefixes.add("/js");
        resourcePrefixes.add("/fonts");
        resourcePrefixes.add("/images");
        resourcePrefixes.add("/favicon.ico");
    }

    private RequestDispatcher defaultRequestDispatcher;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.defaultRequestDispatcher = filterConfig.getServletContext().getNamedDispatcher("default");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if(isResourceUrl(path)) {
            log.debug("###### resource path: {}", path);
            defaultRequestDispatcher.forward(request, response);
        } else {
            chain.doFilter(request, response);  // TODO search: servlet filter chain, order
        }
    }

    private boolean isResourceUrl(String url) {
        for(String prefix : resourcePrefixes) {
            if(url.startsWith(prefix)) return true;
        }
        return false;
    }

    @Override
    public void destroy() {

    }
}
