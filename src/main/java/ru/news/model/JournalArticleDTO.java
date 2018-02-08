package ru.news.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JournalArticleDTO {

    private long groupId;
    private String articleId;
    private String title;
    private String content;
    private Date publishDate;
    private List<String> tags;
    private List<String> category;

}
