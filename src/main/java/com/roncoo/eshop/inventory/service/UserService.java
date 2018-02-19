package com.roncoo.eshop.inventory.service;

import com.roncoo.eshop.inventory.model.User;

import java.util.List;

/**
 * 用户Service接口
 * @author yangfan
 * @date 2018/02/18
 */
public interface UserService {

    User findUserInfo();

    User getCachedUserInfo();
}
