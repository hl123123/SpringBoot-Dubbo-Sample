package com.tyrival.controller.aspect;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.tyrival.controller.annotation.Log;
import com.tyrival.entity.user.User;
import com.tyrival.enums.base.RequestAttrEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/6/23
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * com.tyrival.controller.user.*.*(..))")
    public void log() {
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取请求中由Token拦截器注入的用户信息
        User user = (User) request.getAttribute(RequestAttrEnum.USER.getCode());
        if (user == null || StringUtils.isBlank(user.getId())) {
            // TODO 记录为游客
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        // 类名
        String className = signature.getDeclaringTypeName();

        // 方法名
        Method method = signature.getMethod();
        String methodName = method.getName();

        // 参数
        Object[] arguments = joinPoint.getArgs();

        // 参数名称
        String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();

        Object result = joinPoint.proceed();

        // 如存在Log注解，需要记录日志
        if (method.isAnnotationPresent(Log.class)) {
            Log annotation = method.getAnnotation(Log.class);
            // 获取Log注解内的参数
            String type = annotation.value();

            // TODO 记录日志
            System.out.println("记录日志......");
        }
        return result;
    }
}
