package ru.news.config;

import com.liferay.portal.kernel.portlet.ConfigurationAction;

import javax.portlet.*;

public class NewsPortletConfigurationActionImpl implements ConfigurationAction {

    private static final String PAGE_CONFIGURATION = "/WEB-INF/jsp/newsblock-mvcportlet/configuration.jsp";

    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

    }

    @Override
    public String render(PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        return PAGE_CONFIGURATION;
    }
}
