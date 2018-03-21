package ru.news.search;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import ru.news.model.JournalArticleDTO;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import java.util.List;

public class JournalArticleDTOSearchContainer extends SearchContainer<JournalArticleDTO> {

    private static final String EMPTY_RESULT_MESSAGE = "No records";
    private static final int DELTA = 5;
    private static final String TITLE = "title";
    private static final String TAG = "tag";
//    private static final String CATEGORY = "category";
    private static final String ENABLE_ARCHIVE_NEWS = "enableArchiveNews";
    private static final List<String> HEADER_NAMES = null;

    public JournalArticleDTOSearchContainer(PortletRequest portletRequest, PortletURL iteratorURL) {
        super(portletRequest, new JournalArticleDTODisplayTerms(portletRequest), new JournalArticleDTODisplayTerms(portletRequest), DEFAULT_CUR_PARAM, DELTA, iteratorURL, HEADER_NAMES, EMPTY_RESULT_MESSAGE);
        JournalArticleDTODisplayTerms displayTerms = (JournalArticleDTODisplayTerms) getDisplayTerms();
        iteratorURL.setParameter(TITLE, displayTerms.getTitle());
        iteratorURL.setParameter(TAG, displayTerms.getTag());
//        iteratorURL.setParameter(CATEGORY, displayTerms.getCategory());

        iteratorURL.setParameter(ENABLE_ARCHIVE_NEWS, String.valueOf(displayTerms.getEnableArchiveNews()));
    }
}
