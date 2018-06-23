package com.tyrival.controller.interceptor;

import com.tyrival.entity.user.User;
import com.tyrival.enums.base.RequestAttrEnum;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/6/23
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        // 接受跨域访问
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "x-requested-with, Authorization, Origin, Content-Type, Accept, token, apikey");

        String token = httpServletRequest.getParameter("token");
        User user = new User();

        // TODO 解析TOKEN，验证有效性，将得到的用户信息赋予user，并注入请求

        httpServletRequest.setAttribute(RequestAttrEnum.USER.getCode(), user);

        String newToken = "";

        // TODO 生成新TOKEN

        httpServletResponse.setHeader("token", newToken);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
