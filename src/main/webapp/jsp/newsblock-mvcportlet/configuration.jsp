<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="ru.news.config.NewsPortletConfigurationActionImpl" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib prefix="aui" uri="http://alloy.liferay.com/tld/aui" %>

<liferay-portlet:actionURL var="configurationURL" portletConfiguration="true"/>
<portlet:defineObjects/>
<%
    String showArchiveNews = portletPreferences.getValue(NewsPortletConfigurationActionImpl.ENABLE_ARCHIVE_NEWS, "");
    Boolean showArchiveNewsFlag = GetterUtil.getBoolean(showArchiveNews);
%>

<liferay-ui:success key="config-stored" message="Configuration Saved Successfully"/>

<aui:form method="post" action="<%=configurationURL%>">
    <label>
        <liferay-ui:message key="labelinfo"/>:
        <c:choose>
            <c:when test="<%= showArchiveNewsFlag%>">
                <input class="${NewsPortletConfigurationActionImpl.ENABLE_ARCHIVE_NEWS}" type="checkbox" checked=""
                       name='<portlet:namespace/>enableArchiveNews'>
            </c:when>
            <c:otherwise>
                <input class="${NewsPortletConfigurationActionImpl.ENABLE_ARCHIVE_NEWS}" type="checkbox"
                       name='<portlet:namespace/>enableArchiveNews'>
            </c:otherwise>
        </c:choose>
    </label>
    <button type="submit"><liferay-ui:message key="buttonsave"/></button>
</aui:form>
