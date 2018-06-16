package com.tyrival.annotation;

import java.lang.annotation.*;

/**
 * @Description: 用在Controller层的接口，表示该接口是否需要登录
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
public @interface Login {

    // 是否需要登录（默认为true）
    public boolean value() default true;

}
