package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽微服务 dao层 操作mongodb
 */
public interface SpitDao extends MongoRepository<Spit,String>{
    /**
     * 方法命名查询语句 根据id查询吐槽列表
     * @param parentid
     * @param pageable
     * @return
     */
    Page<Spit> findByParentid(String parentid, Pageable pageable);
}
