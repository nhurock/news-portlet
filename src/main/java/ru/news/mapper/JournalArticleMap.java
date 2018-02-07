package ru.news.mapper;

import com.liferay.portlet.journal.model.JournalArticle;
import ru.news.model.JournalArticleDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class JournalArticleMap {

    private static final Locale LOCALE = Locale.ENGLISH;
    private static final String LANGUAGE_ID = "en_US";

    public static JournalArticleDTO toDto(JournalArticle journalArticle) {

        if (journalArticle == null) return null;

        JournalArticleDTO journalArticleDTO = new JournalArticleDTO();

        journalArticleDTO.setGroupId(journalArticle.getGroupId());
        journalArticleDTO.setArticleId(journalArticle.getArticleId());
        journalArticleDTO.setTitle(journalArticle.getTitle(LOCALE));

        String xmlContentByLocale = journalArticle.getContentByLocale(LANGUAGE_ID);
        String content = SAXMap.getContent(xmlContentByLocale);

        journalArticleDTO.setContent(content);
        journalArticleDTO.setPublishDate(journalArticle.getCreateDate());

        return journalArticleDTO;
    }

    public static List<JournalArticleDTO> toDto(List<JournalArticle> journalArticles) {
        if (journalArticles == null) return null;

        ArrayList<JournalArticleDTO> journalArticleDTOS = new ArrayList<>();
        for (JournalArticle journalArticle : journalArticles) {
            journalArticleDTOS.add(toDto(journalArticle));
        }
        return journalArticleDTOS;
    }

    private static String substring(String message, Integer wordCount) {
        String[] stringsSplit = message.split(" ");
        String[] result;

        if (stringsSplit.length < wordCount) {
            result = stringsSplit;
        } else {
            result = Arrays.copyOfRange(stringsSplit, 0, wordCount);
        }

        StringBuffer stringBuffer = new StringBuffer();
        for (String s : result) {
            stringBuffer.append(s).append(" ");
        }
        return String.valueOf(stringBuffer);
    }
}
