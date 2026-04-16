package perfumeshop.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import perfumeshop.model.User;

public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getServletPath();

        // static resource bỏ qua
        if (url.startsWith("/css") ||
            url.startsWith("/js") ||
            url.startsWith("/images")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        // chưa login
        if (session == null || session.getAttribute("account") == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User u = (User) session.getAttribute("account");

        // không phải admin
        if (u.getRoleID() != 1) {
            res.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // OK pass
        chain.doFilter(request, response);
    }

    @Override public void init(FilterConfig filterConfig) {}
    @Override public void destroy() {}
}