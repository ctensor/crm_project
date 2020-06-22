package com.bjpower.crm.settings;

import com.bjpower.crm.exception.LoginException;
import com.bjpower.crm.settings.domain.User;
import com.bjpower.crm.settings.service.UserService;
import com.bjpower.crm.settings.service.impl.UserServiceImpl;
import com.bjpower.crm.utils.DateTimeUtil;
import com.bjpower.crm.utils.MD5Util;
import javafx.fxml.LoadException;
import org.junit.Test;

import javax.print.DocFlavor;

public class Test1 {
    public static void main(String[] args) {
        //验证失效时间

//        String expireTime = "2019-10-10 12:23:34";
//
//        String currentTime = DateTimeUtil.getSysTime();
//
//        int count = expireTime.compareTo(currentTime);
//        System.out.println(count);


//        String lockState = "0";
//        if ("0".equals(lockState)) {
//            System.out.println("账号已锁定");
//        }
        // 浏览器端的ip地址
//        String ip = "192.168.1.3";
        // 运行访问的ip地址
//        String allowIps = "192.168.1.1,192.168.1.2";
//        if (allowIps.contains(ip)) {
//            System.out.println("有效的ip地址");
//        } else{
//            System.out.println("ip地址受限");
//        }

        String pwd = "123";
        String pwd_md5 = MD5Util.getMD5(pwd);
        System.out.println(pwd_md5);

    }

    @Test
    public void TestUserService() throws LoginException {
        UserService userService = new UserServiceImpl();
        String loginPwd = MD5Util.getMD5("123");
        System.out.println(loginPwd);

        User login = userService.login("ls", loginPwd, "192.168.1.1");

        System.out.println(login);
    }
}
