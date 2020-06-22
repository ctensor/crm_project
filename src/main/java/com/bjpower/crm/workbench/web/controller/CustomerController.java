package com.bjpower.crm.workbench.web.controller;

import com.bjpower.crm.utils.PrintJson;
import com.bjpower.crm.workbench.domain.Customer;
import com.bjpower.crm.workbench.service.CustomerService;
import com.bjpower.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CustomerController extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到客户控制器");

        String path = request.getServletPath();

        if ("/workbench/customer/getCusList.do".equals(path)) {
            getCusList(request, response);
        }
    }

    private void getCusList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取客户列表！");

        CustomerService cs = new CustomerServiceImpl();
        List<Customer> cList = cs.getCusList();

        PrintJson.printJsonObj(response, cList);
    }
}
