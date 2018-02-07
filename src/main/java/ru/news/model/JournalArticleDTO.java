package ru.news.model;

import lombok.Data;

import java.util.Date;

@Data
public class JournalArticleDTO {

    private long groupId;
    private String articleId;
    private String title;
    private String content;
    private Date publishDate;

}
