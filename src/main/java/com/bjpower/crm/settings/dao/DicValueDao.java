package com.bjpower.crm.settings.dao;

import com.bjpower.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValueList(String typeCode);
}
