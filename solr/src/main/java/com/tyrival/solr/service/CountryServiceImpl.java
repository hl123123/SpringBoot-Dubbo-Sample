package com.tyrival.solr.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.tyrival.common.country.CountryService;
import com.tyrival.entity.country.Country;
import com.tyrival.entity.param.QueryParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;

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
@Service
@org.springframework.stereotype.Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private SolrClient client;

    @Override
    public List<Country> listByPage(QueryParam queryParam) {
        SolrQuery query = new SolrQuery();
        int pageIndex = queryParam.getPage().getPageIndex();
        int pageSize = queryParam.getPage().getPageSize();
        query.setStart((pageIndex - 1) * pageSize);
        query.setRows(pageSize);
        StringBuffer buffer = new StringBuffer();
        Object name = queryParam.getConditions().get("name");
        if (name != null && StringUtils.isNotBlank(name.toString())) {
            buffer.append("name:").append("*").append(name.toString()).append("*");
        } else {
            buffer.append("*:*");
        }
        query.set("q", buffer.toString());
        QueryResponse response = null;
        try {
            response = client.query(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Country> list = response.getBeans(Country.class);
        return list;
    }

}