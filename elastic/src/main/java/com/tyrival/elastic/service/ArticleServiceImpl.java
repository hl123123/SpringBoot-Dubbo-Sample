package com.tyrival.elastic.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.tyrival.common.article.ArticleService;
import com.tyrival.elastic.repo.ArticleRepository;
import com.tyrival.entity.article.Article;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
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
@Service
@org.springframework.stereotype.Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void create(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void delete(Article article) {
        articleRepository.delete(article);
    }

    @Override
    public List<Article> list(String keyword) {
        QueryStringQueryBuilder builder = new QueryStringQueryBuilder(keyword);
        Iterable<Article> searchResult = articleRepository.search(builder);
        List<Article> list = new ArrayList<>();
        for (Iterator iter = searchResult.iterator(); iter.hasNext();) {
            list.add((Article) iter.next());
        }
        return list;
    }
}
