package com.github.union.query.dao;

import java.util.List;

import com.github.union.query.models.BaseBean;
import com.github.union.query.models.ParamBean;

/**
 * @author liuhaoyong on 2015/6/18.
 */
public interface AbstractMapper<T extends ParamBean/** 查询条件*/,E extends BaseBean/** 返回类型*/>{

    /**
     * 根据条件查询列表
     * @param t
     * @return
     */
    public List<E> queryByCondition(T t);

    /**
     * 根据条件查询个数
     * @param t
     * @return
     */
    public int countByCondition(T t);

    /**
     * 根据主键查对象
     * @param id
     * @return
     */
    public E queryByPrimaryKey(int id);

    /**
     * 根据条件删除数据
     * @param t
     * @return
     */
    public int deleteByCondition(T t);

    /**
     * 根据条件更新记录
     * @param t
     * @return
     */
    public int updateByPrimaryKey(T t);

    /**
     * 查询数据
     * @param e
     * @return
     */
    public int insert(E e);
}
