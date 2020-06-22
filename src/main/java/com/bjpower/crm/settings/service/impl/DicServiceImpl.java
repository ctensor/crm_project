package com.bjpower.crm.settings.service.impl;

import com.bjpower.crm.settings.dao.DicTypeDao;
import com.bjpower.crm.settings.dao.DicValueDao;
import com.bjpower.crm.settings.domain.DicType;
import com.bjpower.crm.settings.domain.DicValue;
import com.bjpower.crm.settings.service.DicService;
import com.bjpower.crm.utils.SqlSessionUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private SqlSession sqlSession = SqlSessionUtils.getSession();

    DicTypeDao dicTypeDao = sqlSession.getMapper(DicTypeDao.class);
    DicValueDao dicValueDao = sqlSession.getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map = new HashMap<>();

        // 将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        for (DicType dicType : dtList) {

            // 取得每一种类型的字典类型编码
            String typeCode = dicType.getCode();
            // 根据每一个字典类型来取得字典值列表
            List<DicValue> dvList = dicValueDao.getValueList(typeCode);

            map.put(typeCode+"List", dvList);
        }
        return map;

    }
}
