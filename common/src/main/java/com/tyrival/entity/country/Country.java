package com.tyrival.entity.country;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/7/4
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
@SolrDocument(solrCoreName = "tyrival")
public class Country implements Serializable {

    /**
     * id
     */
    @Id
    @Field("id")
    private String id;

    /**
     * 名称
     */
    @Field("name")
    @Indexed
    private String name;

    /**
     * 英文名
     */
    @Field("name_en")
    @Indexed
    private String nameEn;

    /**
     * 所在州
     */
    @Field("continent")
    @Indexed
    private String continent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", continent='" + continent + '\'' +
                '}';
    }
}
