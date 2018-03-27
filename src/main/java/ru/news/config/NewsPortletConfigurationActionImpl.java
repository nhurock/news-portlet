package ru.news.config;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

import javax.portlet.*;

public class NewsPortletConfigurationActionImpl implements ConfigurationAction {

    public static final String ENABLE_ARCHIVE_NEWS = "enableArchiveNews";
    private static final String PAGE_CONFIGURATION = "/WEB-INF/jsp/newsblock-mvcportlet/configuration.jsp";

    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String portletResource = ParamUtil.getString(actionRequest, "portletResource");
        String enableArchiveNews = ParamUtil.get(actionRequest, ENABLE_ARCHIVE_NEWS, "");

        PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(actionRequest, portletResource);
        prefs.setValue(ENABLE_ARCHIVE_NEWS, enableArchiveNews);
        prefs.store();

        SessionMessages.add(actionRequest, "config-stored");
        SessionMessages.add(actionRequest, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_REFRESH_PORTLET, portletResource);
    }

    @Override
    public String render(PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        return PAGE_CONFIGURATION;
    }
}
