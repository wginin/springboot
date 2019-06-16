package com.tensquare.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tensquare.user.pojo.Admin;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody User user) {
        //根据请求中的用户名和密码（登录用户信息），从数据库查询账号和密码是否正确。
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;
    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        /*//1.客户端需要将token信息 通过请求头 传入服务端
        //2.获取客户端请求头中的信息  Authorization ,内容为Bearer+空格+token（前后端商量好的）
        String authorization = request.getHeader("Authorization");///为空？
        if(StringUtils.isEmpty(authorization) || !authorization.startsWith("Bearer ")){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }
        //截取Authorization中的token，验证当前用户是否管理员
        String token = authorization.substring(7);
        //通过工具类调用解析token？
        Claims claims = jwtUtil.parseJWT(token);//头部 载荷 签名
        if(claims == null){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }
        if(!"admin".equals(claims.get("roles"))){
            //判断非管理员  返回权限不足
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }*/
        Claims claims = (Claims)request.getAttribute("admin_claims");
        if(claims == null){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 发送手机验证码
     *
     * @param mobile
     */
    @RequestMapping(value = "/sendsms/{mobile}", method = RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile) {
        userService.sendsms(mobile);
        return new Result(true, StatusCode.OK, "发送验证码成功");
    }


    /**
     * 用户注册
     *
     * @param code
     */
    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result register(@RequestBody Map map,@PathVariable String code) {
        userService.register(code,map);
        return new Result(true, StatusCode.OK, "用户注册成功");
    }


    /**
     * 普通用户登录
     */
    @RequestMapping(value="/login",method= RequestMethod.POST)
    public Result login(@RequestBody Map map ){
        User user = userService.login((String)map.get("mobile"),(String)map.get("password"));
        //验证普通用户密码是否正确
        if(user != null){
            //如果正确就调用JwtUtils.createJWT生成token
            String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");//普通用户角色
            //将token返回给客户端
            //定一个map来存放 token（角色信息） admin信息
            Map rsMap = new HashMap();
            rsMap.put("token",token);//跟前端定义好的key
            rsMap.put("user",user);//管理员信息
            return new Result(true,StatusCode.OK,"登录成功",rsMap);
        }
        return new Result(true,StatusCode.OK,"登录成功",user);
    }


    /**
     * 变更粉丝数 (内部微服务调用)
     */
    @RequestMapping(value="/incfans/{userid}/{x}",method= RequestMethod.POST)
    public void incFanscount(@PathVariable String userid,@PathVariable int x){
        userService.incFanscount(userid,x);//x:粉丝数 增加1 还是减少1     1    -1
    }

    /**
     * 变更关注数
     */
    @RequestMapping(value="/incfollow/{userid}/{x}",method= RequestMethod.POST)
    public void incFollowcount(@PathVariable String userid,@PathVariable int x){
        userService.incFollowcount(userid,x);
    }
}
