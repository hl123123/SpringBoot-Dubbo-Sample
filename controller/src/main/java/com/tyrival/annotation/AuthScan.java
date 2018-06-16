package com.tyrival.annotation;

import java.lang.annotation.*;

/**
 * @Description: 指定扫描用户权限相关注解所在的包
 * @Author: Zhou Chenyu
 * @Date: 2018/6/15
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthScan {
    public String value();
}
