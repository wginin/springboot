package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交友微服务 业务逻辑处理层
 */
@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;


    @Autowired
    private UserClient userClient;
    /**
     * 喜欢 或  不喜欢 操作
     * @param userId 用户id
     * @param friendid 好友id
     * @param friendid
     * @return
     */
    @Transactional
    public int like(String userId, String friendid) {
        //1.根据当前用户id和好友id查询交友表中tb_friend 是否已经关注此好友
        int ucount = friendDao.selectCount(userId,friendid);
        //2.判断已经关注 则return 0 :重复操作
        if(ucount > 0){
            return 0;
        }
        //3.如果未关注 ，往好友表插入记录
        Friend friend = new Friend();
        friend.setUserid(userId);//当前用户id
        friend.setFriendid(friendid);//需要关注的好友id
        friend.setIslike("0");//0：单向喜欢 1：双向喜欢
        friendDao.save(friend);

        ///4.查询对方是否已经关注自己
        int fcount = friendDao.selectCount(friendid,userId);
        //2.判断已经关注 则return 0 :重复操作
        if(fcount > 0){
            ///5.如果对象已经关注自己 更新islike为 1
            //当前用户isLike状态为1
            friendDao.updateFriend(userId,friendid,"1");
            //好友的用户isLike状态为1
            friendDao.updateFriend(friendid,userId,"1");
        }
        ///为用户添加粉丝数和关注数
        userClient.incFanscount(friendid,1);//好友粉丝加1
        userClient.incFollowcount(userId,1);//关注数加1
        return 1;
    }


    @Autowired
    private NoFriendDao noFriendDao;

    /**
     * 向不喜欢列表中添加记录
     * @param userid
     * @param friendid
     */
    public void addNoFriend(String userid,String friendid){
        NoFriend noFriend=new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void deleteFriend(String userid, String friendid) {

        //根据用户id和好友id删除 记录
        friendDao.deleteFriend(userid,friendid);
        ///根据用户id和好友id 更新islike 为 0
        friendDao.updateFriend(friendid,userid,"0");

        userClient.incFanscount(friendid,-1);//好友粉丝加1
        userClient.incFollowcount(userid,-1);//关注数加1
    }
}
