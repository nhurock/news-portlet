<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="ru.news.constant.NewsPortletConstant" %>
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

<%
    String showArchiveNews = portletPreferences.getValue(NewsPortletConstant.ENABLE_ARCHIVE_NEWS, "");
    Boolean showArchiveNewsFlag = GetterUtil.getBoolean(showArchiveNews);

    PortletURL portletURL = renderResponse.createRenderURL();
    String portletURLString = portletURL.toString();
    JournalArticleDTOSearchContainer articleDTOSearchContainer = new JournalArticleDTOSearchContainer(renderRequest, portletURL);
    JournalArticleDTODisplayTerms displayTerms = (JournalArticleDTODisplayTerms) articleDTOSearchContainer.getDisplayTerms();

    displayTerms.setEnableArchiveNews(showArchiveNewsFlag);
    displayTerms.setLocale(user.getLocale());
%>

<aui:form method="POST" action="<%= portletURLString %>">
    <liferay-ui:search-container searchContainer="<%= articleDTOSearchContainer%>"
                                 emptyResultsMessage="search-container.empty-result-message">
        <liferay-ui:search-form page="<%= NewsPortletConstant.PAGE_SEARCH%>"
                                servletContext="<%= application %>"/>
        <liferay-ui:search-container-results
                results="<%= JournalArticleDTOLocalServiceUtil.getJournalArticles(displayTerms, articleDTOSearchContainer.getStart(), articleDTOSearchContainer.getEnd()) %>"
                total="<%= JournalArticleDTOLocalServiceUtil.getTotalJournalArticleCount(displayTerms) %>"
        />

        <liferay-ui:search-container-row className="ru.news.model.JournalArticleDTO" modelVar="news">

            <portlet:renderURL var="getViewNewsURL" windowState="normal">
                <portlet:param name="action" value="<%= NewsPortletConstant.RENDER_SINGLE_NEWS%>"/>
                <portlet:param name="groupId" value="${news.groupId}"/>
                <portlet:param name="articleId" value="${news.articleId}"/>
            </portlet:renderURL>

            <liferay-ui:search-container-column-text href="${getViewNewsURL}"
                                                     name="search-container-column-text.label.title" property="title"/>
            <liferay-ui:search-container-column-text name="search-container-column-text.label.content"
                                                     property="content"/>
            <liferay-ui:search-container-column-text name="search-container-column-text.label.date"
                                                     property="publishDate"/>

        </liferay-ui:search-container-row>
        <liferay-ui:search-iterator/>
    </liferay-ui:search-container>
</aui:form>