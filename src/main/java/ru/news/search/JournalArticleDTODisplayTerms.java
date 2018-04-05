package ru.news.search;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.news.constant.DisplayTermsParam;

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
        title = ParamUtil.getString(portletRequest, DisplayTermsParam.TITLE.getName());
        tag = ParamUtil.getString(portletRequest, DisplayTermsParam.TAG.getName());
        category = ParamUtil.getString(portletRequest, DisplayTermsParam.CATEGORY.getName());
    }
}