package com.tyrival.controller.user;

import com.tyrival.annotation.Login;
import com.tyrival.annotation.Permission;
import com.tyrival.entity.base.*;
import com.tyrival.entity.param.QueryParam;
import com.tyrival.entity.user.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/6/15
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@RestController
public interface UserController {

    /**
     * 登录
     * @param httpReq HTTP请求
     * @param httpRsp HTTP响应
     * @return 创建是否成功
     */
    @GetMapping("/create")
    Result create(HttpServletRequest httpReq, HttpServletResponse httpRsp, User user);

    /**
     * 登出
     * @param httpReq HTTP请求
     * @param httpRsp HTTP响应
     * @return 查询用户列表的结果
     */
    @GetMapping("/list")
    @Login
    @Permission("user:query")
    Result<List<User>> list(HttpServletRequest httpReq, HttpServletResponse httpRsp, QueryParam param);

}
