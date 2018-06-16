package com.tyrival.exception;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/6/15
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public class CommonException extends RuntimeException implements Serializable {
    private ExceptionEnum codeEnum;

    public CommonException(ExceptionEnum codeEnum){
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
    }

    public CommonException(){}

    public ExceptionEnum getCodeEnum() {
        return codeEnum;
    }
}
