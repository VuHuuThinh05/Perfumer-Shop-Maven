package perfumeshop.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getServletPath();

        // bỏ qua static resource
        if (url.startsWith("/css") ||
            url.startsWith("/js") ||
            url.startsWith("/images")) {
            chain.doFilter(request, response);
            return;
        }

        // HOME FILTER không cần chặn login/admin logic
        // chỉ pass thẳng
        chain.doFilter(request, response);
    }

    @Override public void init(FilterConfig filterConfig) {}
    @Override public void destroy() {}
}