package com.bjpower.crm.workbench.dao;


import com.bjpower.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {
    

    Clue detail(String id);

    int save(Clue c);

    Clue getById(String clueId);

    int delete(String clueId);

    List<Clue> getClueList();
}
