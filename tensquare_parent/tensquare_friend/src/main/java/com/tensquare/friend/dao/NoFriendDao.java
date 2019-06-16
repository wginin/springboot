package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 不喜欢列表数据访问层
 */
public interface NoFriendDao extends JpaRepository<NoFriend,String> {

}