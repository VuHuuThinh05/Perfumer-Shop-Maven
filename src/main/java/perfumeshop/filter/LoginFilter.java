package perfumeshop.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import perfumeshop.model.User;

public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getServletPath();

        // luôn cho login đi qua
        if (url.equals("/login")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        // chưa login
        if (session == null || session.getAttribute("account") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // đã login → cho đi tiếp
        chain.doFilter(request, response);
    }

    @Override public void init(FilterConfig filterConfig) {}
    @Override public void destroy() {}
}