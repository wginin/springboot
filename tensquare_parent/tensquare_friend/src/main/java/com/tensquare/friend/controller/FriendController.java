package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 交友微服务控制层
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FriendService friendService;

    /**
     * 关注好友 或 非关注好友
     * friendid:关注好友id
     * type: 1:喜欢 0：不喜欢
     * @return
     */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result like(@PathVariable String friendid,@PathVariable String type){
        //鉴权
        Claims claims = (Claims)request.getAttribute("user_claims");//claims JWT:头部+载荷（claims）+签名
        if(claims == null){
            return  new Result(false, StatusCode.ACCESSERROR,"请登录");
        }
        if("1".equals(type)){//喜欢 需要关注此好友
            //userid: claims.getId()  friendid:friendid
            int rs = friendService.like(claims.getId(),friendid);
            //rs 0:已经关注过好友  1：关注好友成功
            if(rs == 0){
                return  new Result(false, StatusCode.REPERROR,"您已经关注了此好友");
            }
            return  new Result(true, StatusCode.OK,"关注好友成功");
        }
        else
        {
            friendService.addNoFriend(claims.getId(),friendid);//向不喜欢列表中添加记录
        }
        return  new Result(true, StatusCode.OK,"操作成功");
    }


    /**
     *  删除好友
     * @param friendid
     * @param friendid
     * @return
     */
    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result like(@PathVariable String friendid){
        //鉴权
        Claims claims = (Claims)request.getAttribute("user_claims");//claims JWT:头部+载荷（claims）+签名
        if(claims == null){
            return  new Result(false, StatusCode.ACCESSERROR,"请登录");
        }

        friendService.deleteFriend(claims.getId(),friendid);

        return  new Result(true, StatusCode.OK,"删除好友成功");

    }
}
