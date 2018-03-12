package ru.news.mapper;

import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

public class JournalArticleContentSAXMap {

    private static final String ROOT_STATIC_CONTENT = "/root/static-content";

    public static String getContent(String journalArticleContent) {

        Document document = null;
        try {
            document = SAXReaderUtil.read(journalArticleContent);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        if (document != null) {
            Node node = document.selectSingleNode(ROOT_STATIC_CONTENT);
            return node.getStringValue();
        } else return null;
    }
}
