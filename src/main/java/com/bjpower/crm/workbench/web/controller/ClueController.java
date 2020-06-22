package com.bjpower.crm.workbench.web.controller;

import com.bjpower.crm.settings.domain.User;
import com.bjpower.crm.settings.service.UserService;
import com.bjpower.crm.settings.service.impl.UserServiceImpl;
import com.bjpower.crm.utils.DateTimeUtil;
import com.bjpower.crm.utils.PrintJson;
import com.bjpower.crm.utils.UUIDUtil;
import com.bjpower.crm.workbench.dao.ClueDao;
import com.bjpower.crm.workbench.domain.Activity;
import com.bjpower.crm.workbench.domain.Clue;
import com.bjpower.crm.workbench.domain.Tran;
import com.bjpower.crm.workbench.service.ActivityService;
import com.bjpower.crm.workbench.service.ClueService;
import com.bjpower.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpower.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到线索控制器");

        String path = request.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(request, response);
        } else if ("/workbench/clue/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/clue/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/clue/getActivityListByClueId.do".equals(path)) {
            getActivityListByClueId(request, response);
        } else if ("/workbench/clue/unbund.do".equals(path)) {
            unbund(request, response);
        } else if ("/workbench/clue/getActivityListByNameNoByClueId.do".equals(path)) {
            getActivityListByNameNoByClueId(request, response);
        } else if ("/workbench/clue/bund.do".equals(path)) {
            bund(request, response);
        } else if ("/workbench/clue/getActivityListByName.do".equals(path)) {
            getActivityListByName(request, response);
        } else if ("/workbench/clue/convert.do".equals(path)) {
            convert(request, response);
        } else if ("/workbench/clue/getClueList.do".equals(path)) {
            getClueList(request, response);
        }

    }

    private void getClueList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取线索列表");

        ClueService cs = new ClueServiceImpl();
        List<Clue> cList = cs.getClueList();

        PrintJson.printJsonObj(response, cList);
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到线索转换控制器");

        String clueId = request.getParameter("clueId");
        // 接收是否需要创建交易的标记
        String flag = request.getParameter("flag");
        // 获取创建人
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        Tran t = null;
        if ("a".equals(flag)) {

            t = new Tran();
            // 需要创建交易，并接收交易表单中的参数
            String id = UUIDUtil.getUUID();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String createTime = DateTimeUtil.getSysTime();

            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);

        }

        ClueService cs = new ClueServiceImpl();

        /*
            为业务层传递参数：
            1. 必须传递的参数clueId，有了这个clueId之后我们才知道要传递那条记录
            2. 必须传递的参数t，因为在线索转换的过程中，有可能会临时创建一笔交易（业务层接收到的t也有可能是个null）

         */

        boolean flag1 = cs.convert(clueId, t, createBy);

        if (flag1) {
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到模糊查询市场活动列表中");

        String aname = request.getParameter("aname");

        ActivityService as = new ActivityServiceImpl();
        List<Activity> aList = as.getActivityListByName(aname);

        PrintJson.printJsonObj(response, aList);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到关联市场活动操作");

        String cid = request.getParameter("cid");
        String[] aids = request.getParameterValues("aid");

        ClueService cs = new ClueServiceImpl();

        boolean flag = cs.bund(cid, aids);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getActivityListByNameNoByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到模糊查询市场活动列表中(不带线索中已有的)");

        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        Map<String, String> map = new HashMap<>();
        map.put("aname", aname);
        map.put("clueId", clueId);

        ActivityService as = new ActivityServiceImpl();
        List<Activity> aList = as.getActivityListByNameNoByClueId(map);

        PrintJson.printJsonObj(response, aList);

    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到解除关联操作!");

        String id = request.getParameter("id");

        ClueService cs = new ClueServiceImpl();

        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到线索关联的市场活动");

        String clueId = request.getParameter("clueId");

        ActivityService as = new ActivityServiceImpl();

        List<Activity> acList = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response, acList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("跳转到线索详细信息页");

        String id = request.getParameter("id");

        ClueService cs = new ClueServiceImpl();
        Clue c = cs.detail(id);

        request.setAttribute("c", c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到保存线索操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();

        c.setId(id);
        c.setFullname(fullname);
        c.setAppellation(appellation);
        c.setOwner(owner);
        c.setCompany(company);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setAddress(address);

        ClueService cs = new ClueServiceImpl();

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取所以者列表");

        UserService us = new UserServiceImpl();

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);
    }
}
