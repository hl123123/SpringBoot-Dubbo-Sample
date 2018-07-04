package com.tyrival.controller.user;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.tyrival.controller.annotation.Log;
import com.tyrival.controller.annotation.Permission;
import com.tyrival.entity.base.*;
import com.tyrival.common.redis.RedisService;
import com.tyrival.common.user.UserService;
import com.tyrival.entity.param.QueryParam;
import com.tyrival.entity.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
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
public class UserControllerImpl implements UserController {

    @Reference
    private UserService userService;

    @Reference
    private RedisService redisService;

    /** Session有效时间 */
    @Value("${session.expireTime}")
    private long sessionExpireTime;

    @Override
    @Log("insert")
    @Permission
    public Result create(HttpServletRequest request, HttpServletResponse response, User user) {
        user = userService.create(user);
        return new Result(user);
    }

    @Override
    @Log("query")
    public Result list(HttpServletRequest request, HttpServletResponse response, QueryParam queryParam) {
        List<User> list = userService.list(queryParam);
        return new Result(list);
    }

    @Override
    public Result listByPage(HttpServletRequest httpReq, HttpServletResponse httpRsp, QueryParam queryParam) {
        PageResult result = userService.listByPage(queryParam);
        return new Result(result);
    }

}
