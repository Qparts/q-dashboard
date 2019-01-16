package q.app.dashboard.filter;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class NotLoggedFilter implements Filter {

    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.setConfig(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURI = request.getContextPath() + "/login";
        boolean loggedIn = session != null && session.getAttribute("user") != null;
        if (!loggedIn) {
            response.sendRedirect(loginURI);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        setConfig(null);
    }

    public FilterConfig getConfig() {
        return config;
    }

    public void setConfig(FilterConfig config) {
        this.config = config;
    }
}
