package ru.news.service;

import com.liferay.portal.kernel.dao.orm.*;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.journal.model.JournalArticle;
import ru.news.model.JournalArticleDTO;
import ru.news.search.JournalArticleDTODisplayTerms;

import java.util.List;
import java.util.Locale;


public class JournalArticleDTOHelper {

    public static List<JournalArticleDTO> getJournalArticle(JournalArticleDTODisplayTerms displayTerms, int start, int end) {
        List<JournalArticleDTO> articleDTOS = getJournalArticleData(displayTerms);
        if (articleDTOS == null) return null;

        return ListUtil.subList(articleDTOS, start, end);
    }

    /**
     * Возвращает количество записей
     */
    public static int getTotalJournalArticleCount(JournalArticleDTODisplayTerms displayTerms) {
        List<JournalArticleDTO> journalArticleData = getJournalArticleData(displayTerms);
        if (journalArticleData == null) return 0;
        return journalArticleData.size();
    }

    private static List<JournalArticleDTO> getJournalArticleData(JournalArticleDTODisplayTerms displayTerms) {
        List<JournalArticleDTO> journalArticles;
        Locale locale = displayTerms.getLocale();
        if (Validator.isBlank(displayTerms.getKeywords()) && (!displayTerms.isAdvancedSearch())) {
//            Получения данных без фильтров поиска
            journalArticles = JournalArticleDTOLocalServiceUtil.getApprovedAndExpiredJournalArticlesLatestVersion();
        } else {
            ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();
            DynamicQuery dynamicQueryJournalArticle = DynamicQueryFactoryUtil.forClass(JournalArticle.class, "ja", classLoader);
            Junction junction = null;
//            Расширенный поиск
            if (displayTerms.isAdvancedSearch()) {
                if (displayTerms.isAndOperator()) {
                    junction = RestrictionsFactoryUtil.conjunction();
                } else {
                    junction = RestrictionsFactoryUtil.disjunction();
                }

                if (!Validator.isBlank(displayTerms.getTitle())) {
                    junction.add(PropertyFactoryUtil.forName("ja.title").like("%" + displayTerms.getTitle() + "%"));
                }
            } else {
//                 Поиск по основному полю
                junction = RestrictionsFactoryUtil.disjunction();
                junction.add(PropertyFactoryUtil.forName("ja.title").like("%" + displayTerms.getKeywords() + "%"));
                junction.add(PropertyFactoryUtil.forName("ja.content").like("%" + displayTerms.getKeywords() + "%"));
            }
            dynamicQueryJournalArticle.add(junction);
            journalArticles = JournalArticleDTOLocalServiceUtil.getDynamicQuery(dynamicQueryJournalArticle);
        }

//         Фильтрация контента
        if (!displayTerms.getEnableArchiveNews()) {
            // Удаление архивных новостей
            JournalArticleDTOLocalServiceUtil.deleteExpiredJournalArticle(journalArticles);
        }

//         Локализация контента
        LocalisationService.localize(journalArticles, locale);
        return journalArticles;
    }
}
