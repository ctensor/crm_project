package com.bjpower.crm.web.filter;

import com.bjpower.crm.settings.domain.User;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        System.out.println("进入到是否登录过的过滤器");

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getServletPath();
        System.out.println(path);

        // 不应该拦截的资源，自动放行请求
        if ("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)) {
            chain.doFilter(req, resp);

        // 其他资源必须验证有没有登录过
        } else {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                chain.doFilter(req, resp);
            } else {
                // 没有登录过，重定向到登录页
            /*
                    重定向路径怎么写
                        在实际项目开发中，对于路径的使用，不论操作的是前端和后端，应该一律使用绝对路径
                       关于转发和重定向的路径写法如下:
                    转发：
                        使用的是一种特殊的绝对路径使用方式，这种绝对路径前面不加 /项目名，这种路径称为内部路径。
                        /login.jsp
                    重定向：
                        使用的是传统的绝对路径写法，前面必须以/项目名，后面跟具体的资源路径。
                        /crm/login.jsp


                        我们应该在用户跳转的同时，将浏览器地址栏自动设置为登录的路径。为什么使用重定向，使用转发不行吗？
                        转发之后，路径会停留在老路径上，而不是挑战之后最新资源的路径。
             */
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }

    @Override
    public void destroy() {

    }
}