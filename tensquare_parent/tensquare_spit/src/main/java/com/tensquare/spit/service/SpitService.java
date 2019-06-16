package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * 吐槽业务逻辑层
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增 吐槽
     */
    public void save(Spit spit) {
        //设置主键id
        spit.set_id(idWorker.nextId()+"");
        //设置默认值
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        if(!StringUtils.isEmpty(spit.getParentid()) && !spit.getParentid().equals("0")){
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid())); //设置更新 条件
            Update update=new Update();
            update.inc("comment",1); //回复数+1
            mongoTemplate.updateFirst(query,update,"spit");
        }
        spitDao.save(spit);
    }
    /**
     * 删除吐槽
     * @param spitId  吐槽id
     * @return  返回结果
     */
    public void deleteById(String spitId) {
        spitDao.deleteById(spitId);
    }
    /**
     *  修改吐槽
     * @param spit
     * @return
     */
    public void updateById(String spitId, Spit spit) {
        spit.set_id(spitId);
        spitDao.save(spit);
    }
    /**
     *查询所有吐槽列表
     * @return
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }
    /**
     *根据id查询吐槽列表
     * @param spitId
     * @return
     */
    public Spit findById(String spitId) {
        return spitDao.findById(spitId).get();
    }

    public Page<Spit> comment(String parentid, int page, int size) {

        //分页参数对象设置
        Pageable pageable = PageRequest.of(page-1,size);
        //方法命名查询语句
        return spitDao.findByParentid(parentid,pageable);
    }

    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 吐槽点赞
     * @param spitId
     */
    public void thumbup(String spitId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));// where _id = xxx
        //Query:构建更新条件 Update ：更新哪个字段值 collectionanem：集合名词 spit
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");///这个模块更为灵活
    }
}
