package com.bjpower.crm.web.listener;

import com.bjpower.crm.settings.domain.DicValue;
import com.bjpower.crm.settings.service.DicService;
import com.bjpower.crm.settings.service.impl.DicServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {

    /*
        当服务器启动时，上下文域对象创建，对象创建完之后，马上执行该方法

        event: 该参数能够监听的对象，
               我们传递的是上下文域对象，通过该参数就可以取得上下文域对象
     */

    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("上下文域对象被创建了");

        ServletContext application = event.getServletContext();

        DicService ds = new DicServiceImpl();

        /*
            应该管业务层要7个list
            可以打包成为一个Map
                map.put("appellationList", dvList1);
                map.put("clueStateList", dvList2);
                ...
                ...
         */
        Map<String, List<DicValue>> map = ds.getAll();

        // 将map解析为上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for (String key : set) {
            application.setAttribute(key,map.get(key));
        }

    }

}
