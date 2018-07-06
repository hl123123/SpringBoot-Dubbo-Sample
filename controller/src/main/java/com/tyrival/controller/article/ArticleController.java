package com.tyrival.controller.article;

import com.tyrival.entity.article.Article;
import com.tyrival.entity.base.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/6
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@RestController
@RequestMapping("/article")
public interface ArticleController {

    /**
     * 保存
     *
     * @param httpReq
     * @param httpRsp
     * @param article
     * @return
     */
    @GetMapping("/save")
    Result save(HttpServletRequest httpReq, HttpServletResponse httpRsp, Article article);

    /**
     * 删除
     *
     * @param httpReq
     * @param httpRsp
     * @param article
     * @return
     */
    @GetMapping("/delete")
    Result delete(HttpServletRequest httpReq, HttpServletResponse httpRsp, Article article);

    /**
     * 查询
     *
     * @param httpReq
     * @param httpRsp
     * @param keyword
     * @return
     */
    @GetMapping("/list")
    Result list(HttpServletRequest httpReq, HttpServletResponse httpRsp, String keyword);
}
