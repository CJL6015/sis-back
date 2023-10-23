package com.seu.sis.model.entity;

import com.seu.sis.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-08-14 22:49
 */
@Data
@AllArgsConstructor
public class Result<T> implements Serializable {
    private int code;
    private String type;
    private String message;
    private T result;

    public Result(ResultCode code, String message, T result) {
        this.code = code.code;
        this.type = code.type;
        this.message = message;
        this.result = result;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, "ok", data);
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS, "ok", null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCode.FAIL, message, null);
    }

}
