package com.geekbang.springmybatis.work.controller;

import com.geekbang.springmybatis.work.domain.User;
import com.geekbang.springmybatis.work.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-05-11
 */
@RestController
public class MyController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/get")
    public User select(@RequestParam String id) {
        return userService.selectById(id);
    }

    @GetMapping("/list")
    public List<User> list() {
        return userService.selectUsers();
    }

    @GetMapping("/add")
    public void add() {
        userService.addUsers();
    }

}
