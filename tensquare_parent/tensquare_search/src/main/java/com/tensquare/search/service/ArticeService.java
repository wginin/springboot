package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.IdWorker;

/**
 * 搜索文章和 保存文章
 */
@Service
public class ArticeService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    //保存文章
    public void save(Article article){
        article.setId(idWorker.nextId()+"");
        articleDao.save(article);
    }


    /**
     * 根据条件搜索文章数据成功(带分页)
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public Page<Article> searchByKeywords(String keywords, int page, int size) {
        //封装分页对象
        Pageable pageable = PageRequest.of(page-1,size);
        return articleDao.findByTitleOrContentLike(keywords,keywords,pageable);
    }
}
