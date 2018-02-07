package ru.news.mapper;

import com.liferay.portlet.journal.model.JournalArticle;
import ru.news.model.JournalArticleDTO;

import java.text.SimpleDateFormat;
import java.util.*;

public class JournalArticleMap {

    private static final Locale LOCALE = Locale.ENGLISH;
    private static final String LANGUAGE_ID = "en_US";
    private static final Integer WORD_COUNT = 50;
    private static final Integer SENTENCE_COUNT = 2;
    private static final SimpleDateFormat DATE_FORMAT_LONG = new SimpleDateFormat("dd.MM.YYYY 'at' HH:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT_SHORT = new SimpleDateFormat("dd.MM.YYYY");

    public static JournalArticleDTO toDto(JournalArticle journalArticle) {

        if (journalArticle == null) return null;

        JournalArticleDTO journalArticleDTO = new JournalArticleDTO();

        journalArticleDTO.setGroupId(journalArticle.getGroupId());
        journalArticleDTO.setArticleId(journalArticle.getArticleId());
        journalArticleDTO.setTitle(journalArticle.getTitle(LOCALE));

        String xmlContentByLocale = journalArticle.getContentByLocale(LANGUAGE_ID);
        String content = SAXMap.getContent(xmlContentByLocale);

        journalArticleDTO.setContent(content);

//        journalArticleDTO.setPreviewContent(substring(content, WORD_COUNT));

        Date createDate = journalArticle.getCreateDate();

        journalArticleDTO.setDate(DATE_FORMAT_SHORT.format(createDate));

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


    // TODO: решить проблему корректного отображения предложений.
   /* private static String subSentence(String message, Integer sentence) {
        String s = message;
        int indexEndOfSentence = 0;
        for (int i = 0; i < sentence; i++) {
            indexEndOfSentence = s.indexOf(".");
//            System.out.println("index =" + indexEndOfSentence + " " + s.substring(indexEndOfSentence, s.length()));
//            System.out.println(indexEndOfSentence + "  " + s.length() + "  " + substring(s, 30));
            s = s.substring(indexEndOfSentence+2, s.length());
//            System.out.println(indexEndOfSentence + "  " + s.length() + "  " + substring(s, 30));
        }
//        System.out.println(indexEndOfSentence);

        return message.substring(0, indexEndOfSentence-1);
    }*/
}
