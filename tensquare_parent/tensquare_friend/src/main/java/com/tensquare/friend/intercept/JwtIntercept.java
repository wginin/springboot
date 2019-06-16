package com.tensquare.friend.intercept;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截所有的请求（除了login登录）
 */
@Component
public class JwtIntercept extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 方法执行执行 执行此拦截器
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("进入到拦截器了");

        //1.客户端需要将token信息 通过请求头 传入服务端
        //2.获取客户端请求头中的信息  Authorization ,内容为Bearer+空格+token（前后端商量好的）
        /*String authorization = request.getHeader("Authorization");///为空？
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

        String authorization = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer ")){
            //截取Authorization中的token，验证当前用户是否管理员
            String token = authorization.substring(7);
            //通过工具类调用解析token？
            Claims claims = jwtUtil.parseJWT(token);//头部 载荷 签名
            if(claims != null){
                if("admin".equals(claims.get("roles"))){
                    request.setAttribute("admin_claims",claims);//admin_claim当前是管理员
                }
                if("user".equals(claims.get("roles"))){
                    request.setAttribute("user_claims",claims);//admin_claim当前是管理员
                }
            }
        }
       //如果requset中没有admin_claims 或  user_claims 就说明鉴权失败了
        return true;
    }
}
