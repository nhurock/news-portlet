package ru.news.service;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.news.mapper.JournalArticleContentSAXMap;
import ru.news.model.JournalArticleDTO;

import java.util.List;
import java.util.Locale;

/**
 * Переводит поля {@link ru.news.model.JournalArticleDTO} для пользовательского языка.
 */
@Service
public class LocalisationService {

    private static JournalArticleCustomService journalArticleCustomService;

    @Autowired
    public LocalisationService(JournalArticleCustomService journalArticleCustomService) {
        LocalisationService.journalArticleCustomService = journalArticleCustomService;
    }

    public static void localize(JournalArticleDTO journalArticleDTO, Locale locale) {

        JournalArticle journalArticle = journalArticleCustomService.getLatestVersion(journalArticleDTO.getGroupId(), journalArticleDTO.getArticleId());

        String languageIdDefault;
        Locale localeDefault;
        if (locale == null) {
            languageIdDefault = journalArticle.getDefaultLanguageId();
            localeDefault = LanguageUtil.getLocale(languageIdDefault);
        }
            else {
            languageIdDefault = locale.getLanguage();
            localeDefault = locale;
        }

        String title = journalArticle.getTitle(localeDefault);
        String xmlContent = journalArticle.getContentByLocale(languageIdDefault);
        String content = JournalArticleContentSAXMap.getContent(xmlContent);

        journalArticleDTO.setTitle(title);
        journalArticleDTO.setContent(content);
    }

    public static void localize(List<JournalArticleDTO> journalArticleDTOS, Locale locale) {

        for (JournalArticleDTO journalArticleDTO : journalArticleDTOS) {
            localize(journalArticleDTO, locale);
        }
    }
}
