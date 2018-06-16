package com.tyrival.annotation;

import java.lang.annotation.*;

/**
 * @Description: 用在Controller层的接口上，表示访问该接口所需的角色
 * @Author: Zhou Chenyu
 * @Date: 2018/6/15
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Role {
    public String value();
}
