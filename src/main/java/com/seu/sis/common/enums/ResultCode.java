package com.seu.sis.common.enums;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-08-19 13:14
 */
public enum ResultCode {
    /**
     * 响应码枚举
     */
    SUCCESS(0, "成功"),
    FAIL(-1, "失败"),
    UNAUTHORIZED(401, "无权限"),
    NOT_FOUND(404, "接口不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");

    public final int code;

    public final String type;

    ResultCode(int code, String type) {
        this.code = code;
        this.type = type;
    }
}
