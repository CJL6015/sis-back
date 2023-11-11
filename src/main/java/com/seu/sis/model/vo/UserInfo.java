package com.seu.sis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-08-14 22:52
 */
@Data
@Builder
@AllArgsConstructor
public class UserInfo implements Serializable {
    private Long userId;
    private String username;
    private String realName;
    private String avatar;
    private String desc;
    private String password;
    private String token;
    private String homePath;
    private List<Object> roles;

}
