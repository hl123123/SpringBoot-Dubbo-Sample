package com.tyrival.entity.base;

import com.tyrival.entity.param.Page;
import com.tyrival.exception.CommonException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/5/9
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public class Result<T> {
    private Boolean success;

    private String errorCode;

    private String message;

    private T data;

    public Result() {
        this.success = true;
    }

    public Result(T data) {
        this.success = true;
        this.data = data;
    }

    public Result(T data, Page page) {
        this.success = true;
        Map map = new HashMap<>();
        map.put("list", data);
        map.put("page", page);
        this.data = (T) map;
    }

    public Result(CommonException exception) {
        this.success = false;
        this.errorCode = exception.getCodeEnum().getCode();
        this.message = exception.getCodeEnum().getMessage();
    }

    public Result(T data, CommonException exception) {
        this.success = false;
        this.data = data;
        this.errorCode = exception.getCodeEnum().getCode();
        this.message = exception.getCodeEnum().getMessage();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
