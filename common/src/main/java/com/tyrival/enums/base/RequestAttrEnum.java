package com.tyrival.enums.base;

import com.tyrival.enums.BaseEnum;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/6/23
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public enum RequestAttrEnum {

    USER("user");

    private String code;

    RequestAttrEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
