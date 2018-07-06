package com.tyrival.elastic.repo;

        import com.tyrival.entity.article.Article;
        import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
        import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/6
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@Component
public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {
}