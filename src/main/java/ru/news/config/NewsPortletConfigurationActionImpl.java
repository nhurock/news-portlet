package ru.news.config;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import ru.news.NewsPortletConstant;

import javax.portlet.*;

public class NewsPortletConfigurationActionImpl implements ConfigurationAction {

    private static final String PAGE_CONFIGURATION = "/jsp/newsblock-mvcportlet/configuration.jsp";

    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String portletResource = ParamUtil.getString(actionRequest, "portletResource");
        String enableArchiveNews = ParamUtil.get(actionRequest, NewsPortletConstant.ENABLE_ARCHIVE_NEWS, "");

        PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(actionRequest, portletResource);
        prefs.setValue(NewsPortletConstant.ENABLE_ARCHIVE_NEWS, enableArchiveNews);
        prefs.store();

        SessionMessages.add(actionRequest, "config-stored");
        SessionMessages.add(actionRequest, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_REFRESH_PORTLET, portletResource);
    }

    @Override
    public String render(PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        return PAGE_CONFIGURATION;
    }
}
