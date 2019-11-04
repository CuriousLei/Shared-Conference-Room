package com.ximingxing.blog.server.dto;

import com.ximingxing.blog.server.pojo.Label;
import com.ximingxing.blog.server.pojo.Sort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewArticleDTO {
    private String articleTitle;
    private String articleAlias;
    private List<Sort> sorts;
    private List<Label> labels;
    private String articleContent;
    private Integer articleStatus;
}
