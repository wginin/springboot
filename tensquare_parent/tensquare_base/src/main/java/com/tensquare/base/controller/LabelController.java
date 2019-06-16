package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;

import java.util.List;
import java.util.Map;

/**
 * 标签控制层
 */
@RestController
@RequestMapping("/label")
@CrossOrigin //为了解决跨域问题
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 保存
     * @param label
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true, StatusCode.OK,"保存标签成功");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String labelId){
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK,"删除标签成功");
    }


    /**
     * 改
     */
    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)
    public Result deleteById(@PathVariable String labelId,@RequestBody Label label){
        labelService.update(labelId,label);
        return new Result(true, StatusCode.OK,"修改标签成功");
    }



    /**
     * findAll
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        int i = 1 / 0;
        List<Label> labelList = labelService.findAll();
        return new Result(true, StatusCode.OK,"查询所有标签成功",labelList);
    }

    /**
     * findById
     */
    @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String labelId){
        Label label = labelService.findById(labelId);
        return new Result(true, StatusCode.OK,"查询单个标签成功",label);
    }

    /**
     * 根据条件查询标签列表
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findBySearch(@RequestBody Map searchMap){
        List<Label> labelList = labelService.findBySearch(searchMap);
        return new Result(true, StatusCode.OK,"查询单个标签成功",labelList);
    }

    /**
     * 带分页的条件查询
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result findBySearch(@RequestBody Map searchMap,@PathVariable int page,@PathVariable int size){
        Page<Label> labelPage = labelService.findByPageSearch(searchMap,page,size);
        return new Result(true, StatusCode.OK,"查询单个标签成功",new PageResult(labelPage.getTotalElements(),labelPage.getContent()));
    }



}
