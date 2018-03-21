package ru.news.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import ru.news.mapper.JournalArticleContentSAXMap;
import ru.news.model.JournalArticleDTO;

import java.util.List;
import java.util.Locale;

/**
 * Переводит поля {@link ru.news.model.JournalArticleDTO} для пользовательского языка.
 */
public class LocalisationService {

    public static void localize(JournalArticleDTO journalArticleDTO, Locale locale) {

        JournalArticle journalArticle = null;
        try {
            journalArticle = JournalArticleLocalServiceUtil.getLatestArticle(journalArticleDTO.getGroupId(), journalArticleDTO.getArticleId());
        } catch (PortalException | SystemException e) {
            e.printStackTrace();
        }

        if (journalArticle != null) {
            String languageIdDefault;
            Locale localeDefault;
            if (locale == null) {
                languageIdDefault = journalArticle.getDefaultLanguageId();
                localeDefault = LanguageUtil.getLocale(languageIdDefault);
            } else {
                languageIdDefault = locale.toString();
                localeDefault = locale;
            }

            String title = journalArticle.getTitle(localeDefault);
            String xmlContent = journalArticle.getContentByLocale(languageIdDefault);
            String content = JournalArticleContentSAXMap.getContent(xmlContent);

            journalArticleDTO.setTitle(title);
            journalArticleDTO.setContent(content);
        }
    }

    static void localize(List<JournalArticleDTO> journalArticleDTOS, Locale locale) {

        for (JournalArticleDTO journalArticleDTO : journalArticleDTOS) {
            localize(journalArticleDTO, locale);
        }
    }
}
