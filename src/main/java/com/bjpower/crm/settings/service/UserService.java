package com.bjpower.crm.settings.service;

import com.bjpower.crm.exception.LoginException;
import com.bjpower.crm.settings.domain.User;
import javafx.fxml.LoadException;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
