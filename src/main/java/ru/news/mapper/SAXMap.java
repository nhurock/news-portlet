package ru.news.mapper;

import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

public class SAXMap {

    /**
     * Возвращает содержимое JournalArticle's Content.
     */
    public static String getContent(String journalArticleContent) {

        Document document = null;
        try {
            document = SAXReaderUtil.read(journalArticleContent);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        if (document != null) {
            Node node = document.selectSingleNode("/root/static-content");
//            System.out.println(node.getStringValue());
//            System.out.println("");
            return node.getStringValue();
        } else return null;
    }
}
