package com.roncoo.eshop.inventory.controller;

import com.roncoo.eshop.inventory.model.User;
import com.roncoo.eshop.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yangfan
 * @date 2018/02/18
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserInfo")
    @ResponseBody
    public User getUserInfo() {
        return userService.findUserInfo();
    }

    @GetMapping("/getCachedUserInfo")
    @ResponseBody
    public User getCachedUserInfo() {
        return userService.getCachedUserInfo();
    }
}
