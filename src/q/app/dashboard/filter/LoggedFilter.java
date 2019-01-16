package q.app.dashboard.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoggedFilter implements Filter {

    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.setConfig(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (((HttpServletRequest) request).getSession().getAttribute("user") != null) {
            System.out.println("you are logged my friend go to home");
            ((HttpServletResponse) response).sendRedirect("/app/home");
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
