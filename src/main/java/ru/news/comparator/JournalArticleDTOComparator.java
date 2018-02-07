package ru.news.comparator;

import ru.news.model.JournalArticleDTO;

import java.util.Comparator;

public class JournalArticleDTOComparator implements Comparator<JournalArticleDTO> {
    @Override
    public int compare(JournalArticleDTO o1, JournalArticleDTO o2) {
        System.out.println("o1 = [" + o1.getTitle() + "], o2 = [" + o2.getTitle() + "]" + " compare " +  o1.getDate().compareTo(o2.getDate()));
        return o1.getDate().compareTo(o2.getDate());
    }
}
