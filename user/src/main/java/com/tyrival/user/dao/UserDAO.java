package com.tyrival.user.dao;

import com.tyrival.entity.param.QueryParam;
import com.tyrival.entity.user.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

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
@Mapper
@Component
public interface UserDAO {

    /**
     * 创建用户
     * @param User 用户信息
     */
    int create(User User);

    /**
     * 查询用户信息
     * @param queryParam 查询参数
     * @return 查询结果
     */
    List<User> find(@Param("queryParam") QueryParam queryParam);

}
