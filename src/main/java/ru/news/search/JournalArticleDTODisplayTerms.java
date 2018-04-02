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
    private String tag;
    private String category;
    private Boolean enableArchiveNews;
    private Locale locale;

    JournalArticleDTODisplayTerms(PortletRequest portletRequest) {
        super(portletRequest);
        title = ParamUtil.getString(portletRequest, "title");
        tag = ParamUtil.getString(portletRequest, "tag");
        category = ParamUtil.getString(portletRequest, "category");
        enableArchiveNews = ParamUtil.getBoolean(portletRequest, "showArchiveNewsFlag");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getEnableArchiveNews() {
        return enableArchiveNews;
    }

    public void setEnableArchiveNews(Boolean enableArchiveNews) {
        this.enableArchiveNews = enableArchiveNews;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}