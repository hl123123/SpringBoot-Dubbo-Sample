package com.tyrival.entity.base;

import com.tyrival.entity.param.Page;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Zhou Chenyu
 * @Date: 2018/6/17
 * @Version: V1.0
 * @Modified By:
 * @Modified Date:
 * @Why:
 */
public class PageResult<T> implements Serializable {
    private Page page;

    private T list;

    public PageResult() {
        this.page = new Page();
    }

    public PageResult(T list, Page page) {
        this.page = page;
        this.list = list;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public T getList() {
        return list;
    }

    public void setList(T data) {
        this.list = list;
    }
}
