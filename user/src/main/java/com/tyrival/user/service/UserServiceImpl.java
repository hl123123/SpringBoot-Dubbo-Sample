package com.tyrival.user.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tyrival.entity.base.PageResult;
import com.tyrival.entity.param.QueryParam;
import com.tyrival.entity.user.User;
import com.tyrival.common.user.UserService;
import com.tyrival.user.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/6/15
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@Service
@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User create(User user) {
        user.setId(UUID.randomUUID().toString());
        int i = userDAO.create(user);
        return i == 1 ? user : null;
    }

    @Override
    public List<User> list(QueryParam queryParam) {
        List<User> list = userDAO.find(queryParam);
        return list;
    }

    @Override
    public PageResult listByPage(QueryParam queryParam) {
        PageInfo pageInfo = PageHelper.startPage(1, 10)
                .doSelectPageInfo(() -> userDAO.find(queryParam));
        long totalCount = pageInfo.getTotal();
        queryParam.getPage().setTotalCount(totalCount);
        PageResult result = new PageResult(pageInfo.getList(), queryParam.getPage());
        return result;
    }
}
