package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    /**
     * 普通用户登录
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);
    /**
     * 更新粉丝数
     * @param userid 用户ID
     * @param x 粉丝数
     */
    @Modifying
    @Query("update User u set u.fanscount=u.fanscount+?2  where u.id=?1")
    void incFanscount(String userid, int x);
    /**
     * 更新关注数
     * @param userid 用户ID
     * @param x 粉丝数
     */
    @Modifying
    @Query("update User u set u.followcount=u.followcount+?2  where u.id=?1")
    void incFollowcount(String userid, int x);
}
