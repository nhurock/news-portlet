package ru.news.comparator;

import ru.news.model.JournalArticleDTO;

import java.util.Comparator;

public class JournalArticleDTOComparator implements Comparator<JournalArticleDTO> {

    @Override
    public int compare(JournalArticleDTO o1, JournalArticleDTO o2) {
        return o1.getPublishDate().compareTo(o2.getPublishDate());
    }
}
