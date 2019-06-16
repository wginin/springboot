package com.tensquare.base.dao;

import com.tensquare.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 标签接口对象
 * spring data jpa
 * JpaRepository 基础CRUD操作
 * JpaSpecificationExecutor ：带分页的复杂条件查询等
 */
public interface LabelDao extends JpaRepository<Label,String>,JpaSpecificationExecutor<Label>{


}
