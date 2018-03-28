package ru.news.mapper;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

public class JournalArticleContentSAXMap {

    private static final String ROOT_STATIC_CONTENT = "/root/static-content";
    private static final Log log = LogFactoryUtil.getLog(JournalArticleContentSAXMap.class);

    public static String getContent(String journalArticleContent) {
        Document document = null;
        try {
            document = SAXReaderUtil.read(journalArticleContent);
        } catch (DocumentException e) {
            log.error(e);
        }

        if (document == null) return null;

        Node node = document.selectSingleNode(ROOT_STATIC_CONTENT);
        return node.getStringValue();
    }
}
