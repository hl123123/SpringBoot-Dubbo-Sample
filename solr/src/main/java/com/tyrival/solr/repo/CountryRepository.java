package com.tyrival.solr.repo;

import com.tyrival.entity.country.Country;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

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
public interface CountryRepository extends SolrCrudRepository<Country, String> {

    /**
     * 查询名称包含关键词的国家
     * @param name
     * @return
     */
    @Query("name:*?0*")
    List<Country> findByQueryAnnotation(String name);

    /**
     * 查询英文名称包含关键词的国家
     * @param nameEn
     * @return
     */
    List<Country> findByNameEnContaining(String nameEn);
}
