package com.bjpower.crm.activity;

import com.bjpower.crm.workbench.domain.Activity;
import com.bjpower.crm.workbench.service.ActivityService;
import com.bjpower.crm.workbench.service.impl.ActivityServiceImpl;

public class Test {

    @org.junit.Test
    public void TestSave() {
        ActivityService service = new ActivityServiceImpl();

        Activity a = new Activity();
        a.setId("id");
        a.setOwner("owner");
        a.setName("name");
        a.setStartDate("startDate");
        a.setEndDate("endDate");
        a.setCost("cost");
        a.setDescription("description");
        a.setCreateTime("createTime");
        a.setCreateBy("createBy");

        boolean flag = service.save(a);
        System.out.println(flag);
    }

}
