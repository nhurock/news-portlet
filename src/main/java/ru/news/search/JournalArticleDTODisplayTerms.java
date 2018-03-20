package ru.news.search;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.portlet.PortletRequest;
import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Data
public class JournalArticleDTODisplayTerms extends DisplayTerms {

    private String title;
//    private String tag;
//    private String category;
    private Boolean enableArchiveNews;
    private Locale locale;

    public JournalArticleDTODisplayTerms(PortletRequest portletRequest) {
        super(portletRequest);
        title = ParamUtil.getString(portletRequest, "title");
//        tag = ParamUtil.getString(portletRequest, "tag");
//        category = ParamUtil.getString(portletRequest, "category");
        enableArchiveNews = ParamUtil.getBoolean(portletRequest, "showArchiveNewsFlag");
    }
}