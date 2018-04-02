package ru.news.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import ru.news.mapper.JournalArticleContentSAXMap;
import ru.news.model.JournalArticleDTO;

import java.util.List;
import java.util.Locale;

/**
 * Переводит поля {@link ru.news.model.JournalArticleDTO} title и content к пользовательскому языку.
 */
public class LocalisationLocalServiceUtil {

    private static Log log = LogFactoryUtil.getLog(LocalisationLocalServiceUtil.class);

    public static void localize(JournalArticleDTO journalArticleDTO, Locale locale) {
        if (journalArticleDTO == null) {
            throw new IllegalArgumentException("Can't localize null journalArticleDTO.");
        }
        JournalArticle journalArticle = null;
        try {
            journalArticle = JournalArticleLocalServiceUtil.getLatestArticle(journalArticleDTO.getGroupId(), journalArticleDTO.getArticleId());
        } catch (PortalException | SystemException e) {
            log.error("Can't get JournalArticles last version by groupId " + journalArticleDTO.getGroupId() +
                    " and articleId " + journalArticleDTO.getArticleId() + "." + e);
        }

        if (journalArticle == null) {
            throw new IllegalArgumentException("No journalArticle with groupId " + journalArticleDTO.getGroupId() + " and articleId " + journalArticleDTO.getArticleId() + ".");
        }

        String languageIdDefault = GetterUtil.get(locale.toString(), journalArticle.getDefaultLanguageId());
        Locale localeDefault = LanguageUtil.getLocale(languageIdDefault);

        String title = journalArticle.getTitle(localeDefault);
        String xmlContent = journalArticle.getContentByLocale(languageIdDefault);
        String content = JournalArticleContentSAXMap.getContent(xmlContent);

        journalArticleDTO.setTitle(title);
        journalArticleDTO.setContent(content);
    }

    public static void localize(List<JournalArticleDTO> journalArticleDTOS, Locale locale) {
        if (journalArticleDTOS == null) {
            log.warn("Can't localize null List<JournalArticleDTO>.");
            return;
        }
        for (JournalArticleDTO journalArticleDTO : journalArticleDTOS) {
            localize(journalArticleDTO, locale);
        }
    }
}
