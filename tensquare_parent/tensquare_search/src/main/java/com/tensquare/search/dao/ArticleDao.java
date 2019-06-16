package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 搜索文章和 保存文章
 */
public interface ArticleDao extends ElasticsearchRepository<Article,String>{
    /**
     * 根据条件搜索文章数据成功(带分页)  命名查询语句
     * @param keywords
     * @param keywords1
     * @param pageable
     * @return
     */
    Page<Article> findByTitleOrContentLike(String keywords, String keywords1, Pageable pageable);
}
