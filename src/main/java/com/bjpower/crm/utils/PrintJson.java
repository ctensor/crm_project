package com.bjpower.crm.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PrintJson {
    //将boolean 解析成json串
    public static void printJsonFlag(HttpServletResponse response, boolean flag) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", flag);

        ObjectMapper om = new ObjectMapper();
        try {
            //{"success: true}
            String json = om.writeValueAsString(map);
            response.getWriter().print(json);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printJsonObj(HttpServletResponse response, Object obj) {
        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(obj);
            response.getWriter().print(json);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
