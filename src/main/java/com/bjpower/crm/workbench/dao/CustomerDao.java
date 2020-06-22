package com.bjpower.crm.workbench.dao;

import com.bjpower.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    List<Customer> getCusList();

    List<String> getCustomerName(String name);
}
