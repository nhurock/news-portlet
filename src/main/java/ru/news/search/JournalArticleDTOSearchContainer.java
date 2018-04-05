package ru.news.search;

import com.liferay.portal.kernel.dao.search.SearchContainer;
import ru.news.model.JournalArticleDTO;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import java.util.List;

public class JournalArticleDTOSearchContainer extends SearchContainer<JournalArticleDTO> {

    private static final int DELTA = 5;
    private static final List<String> HEADER_NAMES = null;
    private static final String EMPTY_RESULT_MESSAGE = null;

    public JournalArticleDTOSearchContainer(PortletRequest portletRequest, PortletURL iteratorURL) {
        super(portletRequest,
                new JournalArticleDTODisplayTerms(portletRequest),
                new JournalArticleDTODisplayTerms(portletRequest),
                DEFAULT_CUR_PARAM,
                DELTA,
                iteratorURL,
                HEADER_NAMES,
                EMPTY_RESULT_MESSAGE);
    }
}
