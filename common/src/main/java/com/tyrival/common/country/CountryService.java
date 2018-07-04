package com.tyrival.common.country;

import com.tyrival.entity.country.Country;
import com.tyrival.entity.param.QueryParam;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/4
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public interface CountryService {

    /**
     * 分页查询
     * @param queryParam
     * @return
     */
    List<Country> listByPage(QueryParam queryParam);

}
