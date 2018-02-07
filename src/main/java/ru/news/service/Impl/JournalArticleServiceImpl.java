package ru.news.service.Impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import org.springframework.stereotype.Service;
import ru.news.comparator.JournalArticleDTOComparator;
import ru.news.mapper.JournalArticleMap;
import ru.news.model.JournalArticleDTO;
import ru.news.service.JournalArticleService;

import java.util.*;

@Service
public class JournalArticleServiceImpl implements JournalArticleService {

    @Override
    public List<JournalArticleDTO> getJournalArticlesLatestVersion() {
        List<JournalArticleDTO> journalArticleDTOS = JournalArticleMap.toDto(getLatestVersionJA());
        Collections.sort(journalArticleDTOS, new JournalArticleDTOComparator());
        return journalArticleDTOS;
    }

    @Override
    public JournalArticleDTO getJournalArticleLatestVersion(long groupId, String articleId) {
        JournalArticle latestVersion = getLatestVersion(groupId, articleId);
        return JournalArticleMap.toDto(latestVersion);
    }

    private JournalArticle getLatestVersion(long groupId, String articleId) {
        double latestVersion;
        JournalArticle journalArticle = null;
        try {
            latestVersion = JournalArticleLocalServiceUtil.getLatestVersion(groupId, articleId);
            journalArticle = JournalArticleLocalServiceUtil.getArticle(groupId, articleId, latestVersion);
        } catch (PortalException | SystemException e) {
            e.printStackTrace();
        }
        return journalArticle;
    }

    private List<JournalArticle> getLatestVersionJA() {
        HashMap<String, JournalArticle> journalArticleHashMap = new HashMap<>();
        try {
            for (JournalArticle journalArticle : JournalArticleLocalServiceUtil.getArticles()) {
                String articleId = journalArticle.getArticleId();
                if (!journalArticleHashMap.containsKey(articleId)) {
                    journalArticleHashMap.put(articleId, journalArticle);
                }
            }
        } catch (SystemException e) {
            e.printStackTrace();
        }
        ArrayList<JournalArticle> journalArticles = new ArrayList<>(journalArticleHashMap.values());
        return journalArticles;
    }

}
