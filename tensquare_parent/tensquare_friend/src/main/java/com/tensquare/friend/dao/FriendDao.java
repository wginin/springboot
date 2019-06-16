package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 交友微服务 dao层
 */
public interface FriendDao extends JpaRepository<Friend,String> {

    /**
     * 根据当前用户id和好友id查询是否已经关注
     * @param userId
     * @param friendid
     * @return
     */
    @Query("select count(f) from Friend f where f.userid = ?1 and f.friendid = ?2")
    int selectCount(String userId, String friendid);

    /**
     * 根据好友id和用户id更新状态islike 0:单向喜欢 1双向喜欢
     * @param userId
     * @param friendid
     */
    @Query("update Friend  f set f.islike = ?3 where f.userid =?1 and f.friendid = ?2")
    @Modifying
    void updateFriend(String userId, String friendid,String isLike);

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Modifying
    @Query("delete from Friend f where f.userid = ?1 and f.friendid = ?2")
    void deleteFriend(String userid, String friendid);
}
