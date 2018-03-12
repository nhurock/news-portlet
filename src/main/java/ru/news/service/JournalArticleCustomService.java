package ru.news.service;

import com.liferay.portlet.journal.model.JournalArticle;
import ru.news.model.JournalArticleDTO;

import java.util.List;

public interface JournalArticleCustomService {

    List<JournalArticleDTO> getJournalArticlesLatestVersion();

    JournalArticleDTO getJournalArticleLatestVersion(long groupId, String articleId);

    List<JournalArticleDTO> getJournalArticleByTag(String tag);

    List<JournalArticleDTO> getJournalArticleByCategory(String category);

    JournalArticle getLatestVersion(long groupId, String articleId);
}
