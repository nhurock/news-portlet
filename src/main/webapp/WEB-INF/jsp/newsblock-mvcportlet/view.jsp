<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="ru.news.PortletViewController" %>
<%@ page import="ru.news.config.NewsPortletConfigurationActionImpl" %>
<%@ page import="ru.news.search.JournalArticleDTODisplayTerms" %>
<%@ page import="ru.news.search.JournalArticleDTOSearchContainer" %>
<%@ page import="ru.news.service.JournalArticleDTOLocalServiceUtil" %>
<%@ page import="javax.portlet.PortletURL" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="liferay-portlet" uri="http://liferay.com/tld/portlet" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page pageEncoding="UTF-8" %>

<%@include file="init.jsp" %>

<portlet:defineObjects/>
<theme:defineObjects/>

<%
    String showArchiveNews = portletPreferences.getValue(NewsPortletConfigurationActionImpl.ENABLE_ARCHIVE_NEWS, "");
    Boolean showArchiveNewsFlag = GetterUtil.getBoolean(showArchiveNews);

    PortletURL portletURL = renderResponse.createRenderURL();
    String portletURLString = portletURL.toString();
    JournalArticleDTOSearchContainer articleDTOSearchContainer = new JournalArticleDTOSearchContainer(renderRequest, portletURL);
    JournalArticleDTODisplayTerms displayTerms = (JournalArticleDTODisplayTerms) articleDTOSearchContainer.getDisplayTerms();

    displayTerms.setEnableArchiveNews(showArchiveNewsFlag);
    displayTerms.setLocale(user.getLocale());
%>

<liferay-portlet:renderURL varImpl="iteratorURL">
    <portlet:param name="mvcPath" value="view.jsp"/>
</liferay-portlet:renderURL>

<aui:form method="POST" action="<%= portletURLString %>">
    <liferay-ui:search-container searchContainer="<%= articleDTOSearchContainer%>">
        <liferay-ui:search-form page="/WEB-INF/jsp/newsblock-mvcportlet/search.jsp"
                                servletContext="<%= application %>"/>
        <liferay-ui:search-container-results
                results="<%= JournalArticleDTOLocalServiceUtil.getJournalArticles(displayTerms, articleDTOSearchContainer.getStart(), articleDTOSearchContainer.getEnd()) %>"
                total="<%= JournalArticleDTOLocalServiceUtil.getTotalJournalArticleCount(displayTerms) %>"
        />

        <liferay-ui:search-container-row className="ru.news.model.JournalArticleDTO" modelVar="news">

            <portlet:renderURL var="getViewNewsURL" windowState="normal">
                <portlet:param name="action" value="$<%= PortletViewController.RENDER_SINGLE_NEWS%>"/>
                <portlet:param name="groupId" value="${news.groupId}"/>
                <portlet:param name="articleId" value="${news.articleId}"/>
            </portlet:renderURL>

            <liferay-ui:search-container-column-text href="${getViewNewsURL}" name="Title" property="title"/>
            <liferay-ui:search-container-column-text name="Content" property="content"/>
            <liferay-ui:search-container-column-text name="Date" property="publishDate"/>

        </liferay-ui:search-container-row>
        <liferay-ui:search-iterator/>
    </liferay-ui:search-container>
</aui:form>