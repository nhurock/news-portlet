package ru.news.service;

import ru.news.model.JournalArticleDTO;

import java.util.List;

public interface JournalArticleService {

    List<JournalArticleDTO> getJournalArticlesLatestVersion();

    JournalArticleDTO getJournalArticleLatestVersion(long groupId, String articleId);
}
