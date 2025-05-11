package com.seu.sis.controller;

import com.alibaba.fastjson.JSON;
import com.seu.sis.model.entity.Result;
import com.seu.sis.model.vo.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-08-14 22:30
 */
@RestController
public class UserController {

    @GetMapping("/api/test")
    public String test() {
        return "test";
    }

    @GetMapping("/api/getUserInfo")
    public Result<UserInfo> getUserInfo() {
        String user = "{\n" +
                "      userId: '1',\n" +
                "      username: 'admin',\n" +
                "      realName: 'Admin',\n" +
                "      avatar: '',\n" +
                "      desc: 'manager',\n" +
                "      password: '123456',\n" +
                "      token: 'fakeToken1',\n" +
                "      homePath: '/sis/main',\n" +
                "      roles: [\n" +
                "        {\n" +
                "          roleName: 'Super Admin',\n" +
                "          value: 'super',\n" +
                "        },\n" +
                "      ],\n" +
                "    }";
        UserInfo userInfo = JSON.parseObject(user, UserInfo.class);
        return Result.success(userInfo);
    }

    @PostMapping("/api/login")
    public Result<UserInfo> login() {
        String user = "{\n" +
                "      userId: '1',\n" +
                "      username: 'admin',\n" +
                "      realName: 'Admin',\n" +
                "      avatar: '',\n" +
                "      desc: 'manager',\n" +
                "      token: 'fakeToken1',\n" +
                "      homePath: '/sis/main',\n" +
                "      roles: [\n" +
                "        {\n" +
                "          roleName: 'Super Admin',\n" +
                "          value: 'super',\n" +
                "        },\n" +
                "      ],\n" +
                "    }";
        UserInfo userInfo = JSON.parseObject(user, UserInfo.class);
        return Result.success(userInfo);
    }


    @GetMapping("/api/logout")
    public Result<String> logout() {
        return Result.success("登出成功");
    }

}
