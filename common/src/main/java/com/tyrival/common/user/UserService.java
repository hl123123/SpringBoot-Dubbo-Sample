package com.tyrival.common.user;

import com.tyrival.entity.param.QueryParam;
import com.tyrival.entity.user.User;

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
public interface UserService {

    /**
     * 创建用户
     * @param user
     * @return
     */
    User create(User user);

    /**
     * 查询/过滤用户列表
     * @param queryParam
     * @return
     */
    List<User> list(QueryParam queryParam);
}
