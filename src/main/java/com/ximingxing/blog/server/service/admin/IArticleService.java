package com.ximingxing.blog.server.service.admin;

import com.ximingxing.blog.server.common.ServerResponse;
import com.ximingxing.blog.server.pojo.Article;
import com.ximingxing.blog.server.dto.NewArticleDTO;

import java.util.List;

public interface IArticleService {

    /**
     * 通过文章id获取文章信息
     *
     * @param id articleId
     */
    ServerResponse<NewArticleDTO> getArticle(Integer id);

    /**
     * 获取所有文章信息
     */
    ServerResponse<List<Article>> getArticles();

    /**
     * 新建文章前填充表单
     */
    ServerResponse<NewArticleDTO> fillInForm();

    /**
     * 创建新的文章
     */
    ServerResponse<String> createNewArticle(NewArticleDTO newArticleDTO);
}
