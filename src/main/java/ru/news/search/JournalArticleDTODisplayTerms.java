package ru.news.search;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.news.constant.NewsPortletConstant;

import javax.portlet.PortletRequest;
import java.util.Locale;

@EqualsAndHashCode(callSuper = true)
@Data
public class JournalArticleDTODisplayTerms extends DisplayTerms {

    private String title;
    private String tag;
    private String category;
    private Boolean enableArchiveNews;
    private Locale locale;

    JournalArticleDTODisplayTerms(PortletRequest portletRequest) {
        super(portletRequest);
        title = ParamUtil.getString(portletRequest, NewsPortletConstant.DISPLAY_TERMS_PARAM_TITLE);
        tag = ParamUtil.getString(portletRequest, NewsPortletConstant.DISPLAY_TERMS_PARAM_TAG);
        category = ParamUtil.getString(portletRequest, NewsPortletConstant.DISPLAY_TERMS_PARAM_CATEGORY);
    }
}