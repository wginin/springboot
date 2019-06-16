package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticeService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * 文章搜索微服务 控制层
 */
@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticeService articeService;


    /**
     * 保存文章到索引库
     * @return
     */
    @RequestMapping(method= RequestMethod.POST)
    public Result findById(@RequestBody Article article){
        articeService.save(article);
        return new Result(true, StatusCode.OK,"保存文章到索引库成功");
    }


    /**
     *  文章搜索
     * @return
     */
    @RequestMapping(value = "/search/{keywords}/{page}/{size}",method= RequestMethod.GET)
    public Result searchByKeywords(@PathVariable String keywords,@PathVariable int page,@PathVariable int size){
        Page<Article> articlePage = articeService.searchByKeywords(keywords,page,size);
        return new Result(true, StatusCode.OK,"根据条件搜索文章数据成功",new PageResult<>(articlePage.getTotalElements(),articlePage.getContent()));
    }

}
