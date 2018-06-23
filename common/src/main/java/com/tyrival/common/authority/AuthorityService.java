package com.tyrival.common.authority;

import com.tyrival.entity.authority.Authority;
import com.tyrival.entity.param.QueryParam;

import java.util.List;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/6/23
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public interface AuthorityService {

    /**
     * 创建权限
     * @param authority
     * @return
     */
    Authority create(Authority authority);

    /**
     * 查询/过滤权限列表
     * @param queryParam
     * @return
     */
    List<Authority> list(QueryParam queryParam);

}
