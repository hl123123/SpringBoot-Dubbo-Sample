package com.tyrival.controller.user;

import com.tyrival.controller.annotation.Log;
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
@RequestMapping("/user")
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
     * 查询
     * @param httpReq HTTP请求
     * @param httpRsp HTTP响应
     * @param param 查询参数
     * @return 查询用户列表的结果
     */
    @GetMapping("/list")
    Result<List<User>> list(HttpServletRequest httpReq, HttpServletResponse httpRsp, QueryParam param);

    /**
     * 分页查询
     * @param httpReq HTTP请求
     * @param httpRsp HTTP响应
     * @param param 查询参数
     * @return 查询用户列表的结果
     */
    @GetMapping("/list_by_page")
    Result<List<User>> listByPage(HttpServletRequest httpReq, HttpServletResponse httpRsp, QueryParam param);

}
