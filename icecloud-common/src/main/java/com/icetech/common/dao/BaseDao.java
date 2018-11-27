package com.icetech.common.dao;

import com.icetech.common.domain.response.PageQuery;

import java.util.List;

public interface BaseDao<T> {

    /**
     * 保存
     * @param model
     * @return
     */
    Integer insert(T model);

    /**
     * 查询单个结果
     * @param model
     * @return
     */
    T selectById(T model);


    /**
     * 更新
     * @param model
     * @return
     */
    Integer update(T model);

    /**
     * 分页查询结果集
     * @param query
     * @return
     */
    List<T> selectList(PageQuery<T> query);

    /**
     * 根据id 做删除操作
     * @param id
     * @return
     */
    Integer deleteById(Integer id);
}
