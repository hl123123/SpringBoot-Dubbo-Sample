package com.tyrival.controller.aspect;

import com.tyrival.controller.annotation.Permission;
import com.tyrival.entity.user.User;
import com.tyrival.enums.base.RequestAttrEnum;
import com.tyrival.exception.CommonException;
import com.tyrival.exception.ExceptionEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
public class PermissionAspect {

    @Pointcut("execution(public * com.tyrival.controller.user.*.*(..))")
    public void permission(){}

    @Around("permission()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 获取请求中由Token拦截器注入的用户信息
        User user = (User) request.getAttribute(RequestAttrEnum.USER.getCode());

        // 类名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();

        // 方法名
        Method method = signature.getMethod();
        String methodName = method.getName();

        // 包含Permission注解时，进行权限验证
        if (method.isAnnotationPresent(Permission.class)
                || !hasPermission(user, className, methodName)) {
            throw new CommonException(ExceptionEnum.NO_PERMISSION);
        }
        return joinPoint.proceed();
    }

    private boolean hasPermission(User user, String className, String methodName) {

        // TODO 根据用户信息、类名、方法名，查询用户是否有该权限

        return true;
    }
}
