package com.tyrival.controller.country;

import com.tyrival.entity.base.Result;
import com.tyrival.entity.param.QueryParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@RequestMapping("/country")
public interface CountryController {

    /**
     * 分页查询
     * @param httpReq
     * @param httpRsp
     * @param queryParam
     * @return
     */
    @GetMapping("/list_by_page")
    Result listByPage(HttpServletRequest httpReq, HttpServletResponse httpRsp, QueryParam queryParam);
}
