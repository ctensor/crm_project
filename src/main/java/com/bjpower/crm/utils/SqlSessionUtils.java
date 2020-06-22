package com.bjpower.crm.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtils {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            String resource = "mybatis/mybatis-config.xml";
            InputStream resourceAsStream = Resources.getResourceAsStream(resource);

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }

}
