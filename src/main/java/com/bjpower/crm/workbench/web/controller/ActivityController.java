package com.bjpower.crm.workbench.web.controller;

import com.bjpower.crm.settings.domain.User;
import com.bjpower.crm.settings.service.UserService;
import com.bjpower.crm.settings.service.impl.UserServiceImpl;
import com.bjpower.crm.utils.DateTimeUtil;
import com.bjpower.crm.utils.PrintJson;
import com.bjpower.crm.utils.UUIDUtil;
import com.bjpower.crm.vo.PaginationVO;
import com.bjpower.crm.workbench.domain.Activity;
import com.bjpower.crm.workbench.domain.ActivityRemark;
import com.bjpower.crm.workbench.service.ActivityService;
import com.bjpower.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入市场活动控制器");

        String path = request.getServletPath();

        if ("/workbench/activity/getUserList.do".equals(path)) { 
            getUserList(request, response);
        } else if ("/workbench/activity/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/activity/pageList.do".equals(path)) {
            pageList(request, response);
        } else if ("/workbench/activity/delete.do".equals(path)) {
            delete(request, response);
        } else if ("/workbench/activity/getUserListAndActivity.do".equals(path)) {
            getUserListAndActivity(request, response);
        } else if ("/workbench/activity/update.do".equals(path)) {
            update(request, response);
        } else if ("/workbench/activity/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/activity/getRemarkListByAid.do".equals(path)) {
            getRemarkListByAid(request, response);
        } else if ("/workbench/activity/deleteRemark.do".equals(path)) {
            deleteRemark(request, response);
        } else if ("/workbench/activity/saveRemark.do".equals(path)) {
            saveRemark(request, response);
        } else if ("/workbench/activity/updateRemark.do".equals(path)) {
            updateRemark(request, response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改备注信息");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService as = new ActivityServiceImpl();

        boolean flag = as.updateRemark(ar);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);

        PrintJson.printJsonObj(response, map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到保存备注信息");

        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setActivityId(activityId);
        ar.setCreateTime(createTime);
        ar.setNoteContent(noteContent);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);

        ActivityService as = new ActivityServiceImpl();

        boolean flag = as.saveRemark(ar);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("ar", ar);

        PrintJson.printJsonObj(response, map);

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到删除备注");
        String id = request.getParameter("id");

        ActivityService as = new ActivityServiceImpl();

        boolean flag = as.deleteRemark(id);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据市场活动id,取得备注信息列表");

        String activityId = request.getParameter("activityId");

        ActivityService as = new ActivityServiceImpl();

        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response, arList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转到详细信息页的操作");

        String id = request.getParameter("id");

        ActivityService as = new ActivityServiceImpl();

        Activity a = as.detail(id);

        request.setAttribute("a", a);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        // 修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        // 修改人：当前登录的用户
        String editBy = ((User) request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        ActivityService as = new ActivityServiceImpl();

        boolean flag = as.update(a);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取用户信息列表和市场活动");

        String id = request.getParameter("id");

        ActivityService as = new ActivityServiceImpl();

        Map<String, Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response, map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到删除市场活动");

        String[] ids = request.getParameterValues("id");

        ActivityService as = new ActivityServiceImpl();

        boolean flag = as.delete(ids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询市场活动信息列表的操作，条件查询");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        String pageSizeStr = request.getParameter("pageSize");
        // 每页展现的记录数
        int pageSize = Integer.valueOf(pageSizeStr);

        // 计算略过的记录数
        int skipCount = (pageNo - 1) * pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("skipCount", skipCount);
        map.put("pageSize", pageSize);

        ActivityService as = new ActivityServiceImpl();
        /*
                返回一个map还是vo  前端需要市场活动列表和总记录数
                    map:
                        map.put("dataList": dataList);
                        map.put("total": total);
                        PrintJson.printJsonObj(response, map);
                        =====> {"total": 100, "dataList": [{市场活动1},{2},{3}]}
                    vo: PaginationVO<T>
                        private int total;
                        private List<T> dataList;

                        PaginationVO<Activity> vo = new PaginationVO<>;
                        vo.setTotal(total);
                        vo.setDateList(dataList);
                        PrintJson vo --> json
                        将来分页查询，每个模块都有!!!
         */
        PaginationVO<Activity> vo = as.pageList(map);
        // vo --> {"total": 100, "dataList": [{市场活动1},{2},{3}]}
        PrintJson.printJsonObj(response, vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        // 创建人：当前登录的用户
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        ActivityService as = new ActivityServiceImpl();

        boolean flag = as.save(a);

        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");

        UserService us = new UserServiceImpl();

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);
    }
}
