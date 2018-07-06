package com.tyrival.controller.article;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tyrival.common.article.ArticleService;
import com.tyrival.common.article.ArticleService;
import com.tyrival.controller.annotation.Log;
import com.tyrival.controller.annotation.Permission;
import com.tyrival.controller.article.ArticleController;
import com.tyrival.entity.article.Author;
import com.tyrival.entity.base.Result;
import com.tyrival.entity.article.Article;
import com.tyrival.entity.param.QueryParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
public class ArticleControllerImpl implements ArticleController {

    @Reference
    private ArticleService articleService;

    @Override
    @Log("save")
    @Permission
    public Result save(HttpServletRequest httpReq, HttpServletResponse httpRsp, Article article) {

        Author author = new Author();
        author.setId(1L);
        author.setName("毛泽东");
        author.setRemark("");
        article = new Article();
        article.setId(1L);
        article.setTitle("为人民服务");
        article.setContent("向张思德同志学习。");
        article.setAbstracts("党内");
        article.setAuthor(author);
        articleService.create(article);
        return new Result();
    }

    @Override
    @Log("delete")
    @Permission
    public Result delete(HttpServletRequest httpReq, HttpServletResponse httpRsp, Article article) {
        articleService.delete(article);
        return new Result();
    }

    @Override
    @Log("query")
    @Permission
    public Result list(HttpServletRequest httpReq, HttpServletResponse httpRsp, String keyword) {
        List<Article> list = articleService.list(keyword);
        return new Result(list);
    }
}
