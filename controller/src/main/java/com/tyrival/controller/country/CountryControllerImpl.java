package com.tyrival.controller.country;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tyrival.common.country.CountryService;
import com.tyrival.controller.annotation.Log;
import com.tyrival.controller.annotation.Permission;
import com.tyrival.entity.base.Result;
import com.tyrival.entity.country.Country;
import com.tyrival.entity.param.QueryParam;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@RestController
public class CountryControllerImpl implements CountryController {

    @Reference
    private CountryService countryService;

    @Override
    @Log("query")
    @Permission
    public Result listByPage(HttpServletRequest httpReq, HttpServletResponse httpRsp, QueryParam queryParam) {
        List<Country> list = countryService.listByPage(queryParam);
        return new Result(list);
    }
}
