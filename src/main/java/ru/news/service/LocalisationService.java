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

    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private static JournalArticleCustomService journalArticleCustomService;

    @Autowired
    public LocalisationService(JournalArticleCustomService journalArticleCustomService) {
        LocalisationService.journalArticleCustomService = journalArticleCustomService;
    }

    public static void localize(JournalArticleDTO journalArticleDTO, Locale locale) {

        if (locale == null) locale = DEFAULT_LOCALE;

        JournalArticle journalArticle = journalArticleCustomService.getLatestVersion(journalArticleDTO.getGroupId(), journalArticleDTO.getArticleId());
        String languageId = LanguageUtil.getLanguageId(locale);

        String title = journalArticle.getTitle(locale);
        String xmlContent = journalArticle.getContentByLocale(languageId);
        String content = JournalArticleContentSAXMap.getContent(xmlContent);

        journalArticleDTO.setTitle(title);
        journalArticleDTO.setContent(content);
    }

    public static void localize(List<JournalArticleDTO> journalArticleDTOS, Locale locale) {

        if (locale == null) locale = DEFAULT_LOCALE;

        for (JournalArticleDTO journalArticleDTO : journalArticleDTOS) {
            localize(journalArticleDTO, locale);
        }
    }
}
