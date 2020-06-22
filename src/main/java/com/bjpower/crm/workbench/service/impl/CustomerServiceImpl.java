package com.bjpower.crm.workbench.service.impl;

import com.bjpower.crm.utils.SqlSessionUtils;
import com.bjpower.crm.workbench.dao.CustomerDao;
import com.bjpower.crm.workbench.domain.Customer;
import com.bjpower.crm.workbench.service.CustomerService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private SqlSession sqlSession = SqlSessionUtils.getSession();
    private CustomerDao customerDao = sqlSession.getMapper(CustomerDao.class);

    @Override
    public List<Customer> getCusList() {

        List<Customer> cList = customerDao.getCusList();
        return cList;
    }

    @Override
    public List<String> getCustomerName(String name) {
        List<String> cList = customerDao.getCustomerName(name);
        return cList;
    }
}
