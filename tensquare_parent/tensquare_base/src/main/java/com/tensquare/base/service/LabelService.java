package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标签业务逻辑处理类
 */
@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao;

    //注入idWork
    @Autowired
    private IdWorker idWorker;

    //增
    public void save(Label label){
        //分布式系统 id使用idwork
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    /**
     * 根据id删除标签
     * @param labelId
     */
    public void deleteById(String labelId) {
        labelDao.deleteById(labelId);
    }

    /**
     * 更新标签信息
     * @param labelId
     * @param label
     */
    public void update(String labelId, Label label) {
        label.setId(labelId);
        //先查询再更新
        labelDao.save(label);
    }

    /**
     * 查询所有标签数据
     * @return
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 根据标签id获取标签信息
     * @param labelId
     * @return
     */
    public Label findById(String labelId) {
        return labelDao.findById(labelId).get();
    }

    /**
     * 根据条件查询标签列表
     */
    public List<Label> findBySearch(Map searchMap) {

        return labelDao.findAll(getSpecification(searchMap));
    }

    /**
     * 带分页的条件查询
     */



    /**
     * 公共查询条件抽取
     */
    public Specification<Label> getSpecification(Map searchMap){
        //匿名类 通过toPredicate拼接查询条件
        return new Specification<Label>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //定一个list集合存放Predicate对象
                List<Predicate> predicateList = new ArrayList<>();
                if(!StringUtils.isEmpty(searchMap.get("labelname"))){
                    //拼接查询条件  参数1:labelname like   参数2：searchMap.get("labelname")   labelname like  '%%'
                    Predicate p1 = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + searchMap.get("labelname") + "%");
                    predicateList.add(p1);
                }

                if(!StringUtils.isEmpty(searchMap.get("state"))){
                    //拼接查询条件  参数1:labelname like   参数2：searchMap.get("labelname")   labelname like  '%%'
                    Predicate p1 = criteriaBuilder.like(root.get("state").as(String.class),"%"+searchMap.get("state") +"%");
                    predicateList.add(p1);
                }

                if(!StringUtils.isEmpty(searchMap.get("count"))){
                    //拼接查询条件  参数1:labelname like   参数2：searchMap.get("labelname")   labelname like  '%%'
                    Predicate p1 = criteriaBuilder.like(root.get("count").as(String.class),"%"+searchMap.get("count") +"%");
                    predicateList.add(p1);
                }

                if(!StringUtils.isEmpty(searchMap.get("recommend"))){
                    //拼接查询条件  参数1:labelname like   参数2：searchMap.get("labelname")   labelname like  '%%'
                    Predicate p1 = criteriaBuilder.like(root.get("recommend").as(String.class),"%"+searchMap.get("recommend") +"%");
                    predicateList.add(p1);
                }
                if(predicateList ==null || predicateList.size() ==0){
                    return null;
                }
                /*Predicate[] predicates = new Predicate[predicateList.size()];
                //将list集合转成predicate数组
                //将list集合通过定义好的数组类型进行转换
                Predicate[] newPredicate = predicateList.toArray(predicates);
                //最终条件就构建好了
                return criteriaBuilder.and(newPredicate);*/
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }
    /**
     * 带分页的条件查询
     */
    public Page<Label> findByPageSearch(Map searchMap,int page,int size) {
        Pageable pageable = PageRequest.of(page-1,size);//设置当前页码 和每页显示记录数 页码从1开始 pageable从0开始
        return labelDao.findAll(getSpecification(searchMap),pageable);
    }
}
