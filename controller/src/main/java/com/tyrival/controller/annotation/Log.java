package com.tyrival.controller.annotation;

import java.lang.annotation.*;

/**
 * @Description: 通过AOP记录日志的注解
 * @Author: Zhou Chenyu
 * @Date: 2018/6/23
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String value();
}
