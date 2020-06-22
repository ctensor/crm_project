package com.bjpower.crm.workbench.service;

import com.bjpower.crm.workbench.domain.Clue;
import com.bjpower.crm.workbench.domain.Tran;

import java.util.List;

public interface ClueService {
    boolean save(Clue c);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);

    List<Clue> getClueList();
}
