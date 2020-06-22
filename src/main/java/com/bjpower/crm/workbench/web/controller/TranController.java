package com.bjpower.crm.workbench.web.controller;

import com.bjpower.crm.settings.domain.User;
import com.bjpower.crm.settings.service.UserService;
import com.bjpower.crm.settings.service.impl.UserServiceImpl;
import com.bjpower.crm.utils.PrintJson;
import com.bjpower.crm.workbench.dao.CustomerDao;
import com.bjpower.crm.workbench.domain.Customer;
import com.bjpower.crm.workbench.service.CustomerService;
import com.bjpower.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TranController extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到交易处理控制器");

        String path = request.getServletPath();

        if ("/workbench/transaction/add.do".equals(path)) {
            add(request, response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询客户名称");

        String name = request.getParameter("name");

        CustomerService c = new CustomerServiceImpl();
        List<String> cList = c.getCustomerName(name);

        PrintJson.printJsonObj(response, cList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转到交易添加页的操作");

        UserService us = new UserServiceImpl();

        List<User> uList = us.getUserList();

        request.setAttribute("uList", uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }
}
