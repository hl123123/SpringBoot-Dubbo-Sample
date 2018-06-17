package com.tyrival.entity.param;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/6/15
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public class QueryParam implements Serializable {

    private Page page;
    private Map<String, Object> conditions;

    public QueryParam() {
        this.page = new Page();
        this.conditions = new HashMap<String, Object>();
    }

    public QueryParam(Page page) {
        this.conditions = new HashMap<String, Object>();
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }

    public void addCondition(String k, Object v) {
        this.conditions.put(k, v);
    }

    public void removeCondition(String k) {
        if (this.conditions.get(k) != null) {
            this.conditions.remove(k);
        }
    }

    public void clearCondition() {
        this.conditions = new HashMap<>();
    }
}
