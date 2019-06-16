package com.tensquare.user.service;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.tensquare.user.pojo.Admin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import util.IdWorker;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /**
     * 增加
     *
     * @param user
     */
    public void add(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));//对管理员密码进行加密
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送短信验证码
     *
     * @param mobile
     */
    public void sendsms(String mobile) {

        //短信验证码规则  100000 - 999999  6为验证码
        int min = 100000;
        int max = 999999;
        Random random = new Random();
        int randomNum = random.nextInt(max);//随机6位 有可能5 4位等
        if (randomNum < min) {
            randomNum = randomNum + min;//6位随机码
        }
        ///验证码生成了 Logger日志对象打印日志信息
        System.out.println("手机号码：" + mobile + "验证码" + randomNum);

        //调用RabbitMQ将验证码消息 发送到sms消息队列中
        Map<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("code",randomNum+"");//以字符串形式存储
        rabbitTemplate.convertAndSend("sms",map);//将手机号码和验证码放入消息队列
        //将验证码放入redis中（后续注册时验证 验证码是否正确） 设置5分钟过期
        redisTemplate.opsForValue().set("code_"+mobile,randomNum+"");//key:如何定义？ "验证码_"+手机号码  key是否唯一？唯一 标识是什么业务？

        System.out.println("发送验证码到此已经执行成功了");
    }

    /**
     * 用户注册
     * @param code
     * @param map
     */
    public void register(String code, Map map) {

        //判断验证码是否正确
        //code:页面上的验证码
        //redis中的验证码
        String redisCode = (String)redisTemplate.opsForValue().get("code_"+map.get("mobile"));
        if(StringUtils.isEmpty(redisCode) || StringUtils.isEmpty(code)){
            throw new RuntimeException("验证不能为空");
        }

        //判断两个验证码是否一致
        if(!code.equals(redisCode)){
            throw new RuntimeException("验证输入错误");
        }
        User user = new User();
        user.setId(idWorker.nextId()+"");//主键id
        user.setMobile(map.get("mobile")+"");//设置手机号码
        user.setPassword(map.get("password")+"");//设置密码
        user.setNickname(map.get("nickname")+"");//昵称
        user.setRegdate(new Date());//注册日期
        user.setUpdatedate(new Date());//修改日期
        user.setOnline(0l);//在线时长
        user.setFanscount(0);//粉丝数
        user.setFollowcount(0);//关注数
        userDao.save(user);
    }

    /**
     * 普通用户登录
     * @param mobile
     * @param password
     * @return
     */
    public User login(String mobile, String password) {
        //是不是直接根据账号和密码 查询管理员？ select * from t_admin where loginname = ?
        User user  = userDao.findByMobile(mobile);
        if(bCryptPasswordEncoder.matches(password,user.getPassword())){
            return user;
        }
        return null;
    }
    /**
     * 变更粉丝数 (内部微服务调用)
     */
    @Transactional
    public void incFanscount(String userid, int x) {
        userDao.incFanscount(userid,x);
    }
    /**
     * 变更关注数
     */
    @Transactional
    public void incFollowcount(String userid, int x) {
        userDao.incFollowcount(userid,x);
    }
}
