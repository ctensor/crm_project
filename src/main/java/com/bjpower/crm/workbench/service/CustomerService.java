package com.bjpower.crm.workbench.service;

import com.bjpower.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getCusList();

    List<String> getCustomerName(String name);
}
