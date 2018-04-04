<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="ru.news.constant.NewsPortletConstant" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib prefix="aui" uri="http://alloy.liferay.com/tld/aui" %>

<liferay-portlet:actionURL var="configurationURL" portletConfiguration="true"/>
<portlet:defineObjects/>
<%
    String enableArchiveNews = NewsPortletConstant.ENABLE_ARCHIVE_NEWS;
    String showArchiveNews = portletPreferences.getValue(enableArchiveNews, "");
    Boolean showArchiveNewsFlag = GetterUtil.getBoolean(showArchiveNews);
%>

<liferay-ui:success key="<%=NewsPortletConstant.ACTION_REQUEST_KEY_CONFIG_STORED %>"
                    message="portlet.configuration.label.configuration-saved-success"/>

<aui:form method="post" action="<%=configurationURL %>">
    <label>
        <liferay-ui:message key="portlet.configuration.label.view-archive-news"/>:
        <c:choose>
            <c:when test="<%=showArchiveNewsFlag %>">
                <input class="<%=enableArchiveNews %>" type="checkbox" checked=""
                       name='<portlet:namespace/>enableArchiveNews'>
            </c:when>
            <c:otherwise>
                <input class="<%=enableArchiveNews %>" type="checkbox"
                       name='<portlet:namespace/>enableArchiveNews'>
            </c:otherwise>
        </c:choose>
    </label>
    <button type="submit"><liferay-ui:message key="portlet.configuration.button.save-view-archive-news"/></button>
</aui:form>
