package com.tyrival.exception;

import java.io.Serializable;

import static com.tyrival.exception.ExpCodePrefix.COMMON_EXP_PREFIX;
import static com.tyrival.exception.ExpCodePrefix.USER_EXP_PREFIX;


/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/6/15
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public enum ExceptionEnum implements Serializable {

    /** 通用异常 */
    UNKNOW_ERROR(COMMON_EXP_PREFIX + "000", "未知异常"),
    ERROR_404(COMMON_EXP_PREFIX + "001", "没有该接口"),
    PARAM_NULL(COMMON_EXP_PREFIX + "002", "参数为空"),
    NO_REPEAT(COMMON_EXP_PREFIX + "003", "请勿重复提交"),
    SESSION_NULL(COMMON_EXP_PREFIX + "004", "请求头中SessionId不存在"),
    HTTP_REQ_METHOD_ERROR(COMMON_EXP_PREFIX + "005", "HTTP请求方法不正确"),
    JSON_ERROR(COMMON_EXP_PREFIX + "006", "JSON解析异常"),
    NO_PERMISSION(COMMON_EXP_PREFIX + "007", "当前用户无此操作权限"),

    /** User模块异常 */
    ACCOUNT_NULL(USER_EXP_PREFIX + "000", "用户名为空"),
    PASSWORD_NULL(USER_EXP_PREFIX + "001", "密码为空");

    private String code;
    private String message;

    ExceptionEnum(){}

    ExceptionEnum(String code, String message){
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
