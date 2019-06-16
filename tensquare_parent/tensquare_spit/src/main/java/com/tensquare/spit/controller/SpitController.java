package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 吐槽微服务控制层
 */
@RestController
@RequestMapping("/spit")
@CrossOrigin
public class SpitController {

    @Autowired
    private SpitService spitService;

    /**
     * 增 吐槽
     */
    @RequestMapping(method= RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(true, StatusCode.OK,"增加吐槽信息成功");
    }

    /**
     * 删除吐槽
     * @param spitId  吐槽id
     * @return  返回结果
     */
    @RequestMapping(value = "/{spitId}",method= RequestMethod.DELETE)
    public Result deleteById(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK,"删除吐槽信息成功");
    }


    /**
     *  修改吐槽
     * @param spit
     * @return
     */
    @RequestMapping(value = "/{spitId}",method= RequestMethod.PUT)
    public Result updateById(@PathVariable String spitId,@RequestBody Spit spit){
        spitService.updateById(spitId,spit);
        return new Result(true, StatusCode.OK,"修改吐槽信息成功");
    }


    /**
     *查询所有吐槽列表
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        List<Spit> spitList = spitService.findAll();
        return new Result(true, StatusCode.OK,"查询所有吐槽信息成功",spitList);
    }

    /**
     *根据id查询吐槽列表
     * @param spitId
     * @return
     */
    @RequestMapping(value = "/{spitId}",method= RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        Spit spit = spitService.findById(spitId);
        return new Result(true, StatusCode.OK,"根据吐槽id查询吐槽信息成功",spit);
    }


    /**
     * 根据上级ID查询吐槽列表
     *
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method= RequestMethod.GET)
    public Result comment(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
        Page<Spit> spitPage = spitService.comment(parentid,page,size);
        return new Result(true, StatusCode.OK,"根据上级ID查询吐槽列表成功",new PageResult<>(spitPage.getTotalElements(),spitPage.getContent()));
    }


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 吐槽点赞
     */
    @RequestMapping(value = "/thumbup/{spitId}",method= RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId){
        String userid = "1113";//后续会改成动态的
        //1.判断当前用户是否已经点赞
        if(!StringUtils.isEmpty(redisTemplate.opsForValue().get("thumbup_"+userid+"_"+spitId))){
            //2.如果已经点赞则返回错误信息
            return new Result(false, StatusCode.REPERROR,"您已经点赞了");
        }
        //3.如果未点赞则 调用service层进行点赞
        spitService.thumbup(spitId);
        //4.点赞成功后 将点赞key存入redis
        redisTemplate.opsForValue().set("thumbup_"+userid+"_"+spitId,"1");
        return new Result(true, StatusCode.OK,"吐槽点赞成功");
    }


}
