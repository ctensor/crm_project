package com.bjpower.crm.workbench.dao;

import com.bjpower.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClued(String clueId);

    int delete(ClueRemark clueRemark);
}
