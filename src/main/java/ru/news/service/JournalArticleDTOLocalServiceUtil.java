package ru.news.service;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import ru.news.comparator.JournalArticleDTOComparator;
import ru.news.mapper.JournalArticleMap;
import ru.news.model.JournalArticleDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class JournalArticleDTOLocalServiceUtil {

    /**
     * Удаяет архивные записи из коллекции
     *
     * @param journalArticleDTOS список новстей.
     */
    static void deleteExpiredJournalArticle(List<JournalArticleDTO> journalArticleDTOS) {
        ListIterator<JournalArticleDTO> iterator = journalArticleDTOS.listIterator();
        while (iterator.hasNext()) {
            if (isExpired(iterator.next())) {
                iterator.remove();
            }
        }
    }

    /**
     * Возвращает список актуальных {@link JournalArticleDTO} cо статусом Approved и Expired
     */
    static List<JournalArticleDTO> getApprovedAndExpiredJournalArticlesLatestVersion() {
        List<JournalArticleDTO> journalArticleDTOS = JournalArticleMap.toDto(getLatestVersionApprovedAndExpiredJournalArticle());
        journalArticleDTOS.sort(new JournalArticleDTOComparator().reversed());
        return journalArticleDTOS;
    }

    /**
     * Возвращает список актуальных {@link JournalArticleDTO}
     *
     * @param articleId ID {@link JournalArticle}
     * @param groupId   groupId {@link JournalArticle}
     */
    public static JournalArticleDTO getJournalArticleLatestVersion(long groupId, String articleId) {
        JournalArticle latestVersion = getLatestVersion(groupId, articleId);
        return JournalArticleMap.toDto(latestVersion);
    }

    /**
     * Возвращает последнюю версию WebContent {@link JournalArticle}
     *
     * @param articleId ID {@link JournalArticle}
     * @param groupId   groupId {@link JournalArticle}
     */
    static JournalArticle getLatestVersion(long groupId, String articleId) {
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

    /**
     * Возвращает новости из запроса {@link DynamicQuery}
     *
     * @param dynamicQuery для поиска {@link JournalArticle}
     */
    static List<JournalArticleDTO> getDynamicQuery(DynamicQuery dynamicQuery) {
        List<JournalArticle> dynamicQuery1 = null;
        try {
            dynamicQuery1 = JournalArticleLocalServiceUtil.dynamicQuery(dynamicQuery);
        } catch (SystemException e) {
            e.printStackTrace();
        }

        if (dynamicQuery1 == null) {
            return null;
        }

        HashMap<String, JournalArticle> journalArticleHashMap = new HashMap<>();
        for (JournalArticle journalArticle : dynamicQuery1) {
            String articleId = journalArticle.getArticleId();
            if (!journalArticleHashMap.containsKey(articleId)) {
                journalArticleHashMap.put(articleId, journalArticle);
            }
        }
        List<JournalArticle> resultJournalArticle = new ArrayList<>();
        resultJournalArticle.addAll(journalArticleHashMap.values());
        return JournalArticleMap.toDto(resultJournalArticle);
    }

    /**
     * Возвращает список актуальных {@link JournalArticle} со статусом Approved и Expired
     */
    private static List<JournalArticle> getLatestVersionApprovedAndExpiredJournalArticle() {
        HashMap<String, JournalArticle> journalArticleHashMap = new HashMap<>();
        try {
            for (JournalArticle journalArticle : JournalArticleLocalServiceUtil.getArticles()) {
                String articleId = journalArticle.getArticleId();
                long groupId = journalArticle.getGroupId();

                if (!journalArticle.isInTrash() && (journalArticle.isApproved() || journalArticle.isExpired()))
                    if (!journalArticleHashMap.containsKey(articleId)) {
                        journalArticleHashMap.put(articleId, getLatestVersion(groupId, articleId));
                    }
            }
        } catch (SystemException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(journalArticleHashMap.values());
    }

    /**
     * Возращает true если запись имеент статус Expired
     */
    private static Boolean isExpired(JournalArticleDTO journalArticleDTO) {
        JournalArticle journalArticle = getLatestVersion(journalArticleDTO.getGroupId(), journalArticleDTO.getArticleId());
        return journalArticle.isExpired();
    }
}