<%@ page import="ru.news.search.JournalArticleDTODisplayTerms" %>
<%@ page import="ru.news.search.JournalArticleDTOSearchContainer" %>
<%@ page import="ru.news.service.JournalArticleDTOHelper" %>
<%@ page import="javax.portlet.PortletURL" %>
<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="liferay-portlet" uri="http://liferay.com/tld/portlet" %>

<%@include file="init.jsp" %>
<%
    String showArchiveNews = portletPreferences.getValue("enableArchiveNews", "");
    Boolean showArchiveNewsFlag;
    showArchiveNewsFlag = Objects.equals(showArchiveNews, "on");

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
                results="<%= JournalArticleDTOHelper.getJournalArticle(displayTerms, articleDTOSearchContainer.getStart(), articleDTOSearchContainer.getEnd()) %>"
                total="<%= JournalArticleDTOHelper.getTotalJournalArticleCount(displayTerms) %>"
        />

        <liferay-ui:search-container-row className="ru.news.model.JournalArticleDTO" modelVar="news">

            <portlet:renderURL var="getViewNewsURL" windowState="normal">
                <portlet:param name="action" value="renderSingleNews"/>
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