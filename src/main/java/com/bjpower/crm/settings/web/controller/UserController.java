package com.bjpower.crm.settings.web.controller;

import com.bjpower.crm.settings.domain.User;
import com.bjpower.crm.settings.service.UserService;
import com.bjpower.crm.settings.service.impl.UserServiceImpl;
import com.bjpower.crm.utils.MD5Util;
import com.bjpower.crm.utils.PrintJson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入用户控制器");

        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path)) {
            login(request, response);
        } else if ("settings/user/xxx.do".equals(path)) {
//            xxx.(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到验证登录操作");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");

        // 将密码的明文形式转换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        // 获取浏览器的ip地址
        String ip = request.getRemoteAddr();
        System.out.println("ip-------------:" + ip);

        // 未来业务层，统一使用代理类形态的接口对象
//        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        UserService us = new UserServiceImpl();

        try {
            User user = us.login(loginAct, loginPwd, ip);
            request.getSession().setAttribute("user", user);

            // 如果程序执行到此处，说明业务层没有为Contorller抛出任何异常
            // 表示登录成功
            /*
                {"success": true}
             */
            PrintJson.printJsonFlag(response, true);
        } catch (Exception e) {
            e.printStackTrace();
            // 一旦程序执行了catch块的信息，说明业务层为我们验证登录失败，为controller抛了异常
            // 表示登录失败
            /*
                 {"success":false, "msg":?}
             */
            String msg = e.getMessage();
            /*
                    我们现在作为controller, 需要为ajax请求提供多项信息

                    可以有两种手段来处理：
                        （1） 将多项信息打包成map, 将map 解析为json串
                        （2） 创建一个Vo
                                private boolean success;
                                private String msg;

                    如果对于展现的信息将来还会大量使用，我们创建一个Vo类，使用方便
                    如果对于展现的信息只在这个需求中使用，我们使用map就可以了
             */
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);
        }
    }
}
