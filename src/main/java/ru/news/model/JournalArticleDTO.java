package ru.news.model;

import lombok.Data;

@Data
public class JournalArticleDTO{

    private long groupId;
    private String articleId;
    private String title;
    private String content;
    private String previewContent;
    private String date;

}
