package com.tyrival.common.article;

import com.tyrival.entity.article.Article;

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
public interface ArticleService {

    /**
     * 创建文章
     * @param article
     * @return
     */
    void create(Article article);

    /**
     * 删除文章
     * @param article
     * @return
     */
    void delete(Article article);

    /**
     * 查询文章
     * @param keyword
     * @return
     */
    List<Article> list(String keyword);

}
