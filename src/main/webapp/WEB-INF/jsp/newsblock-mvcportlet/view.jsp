<%@ page import="com.liferay.portal.kernel.dao.search.DisplayTerms" %>
<%@ page import="com.liferay.portal.kernel.util.ListUtil" %>
<%@ page import="ru.news.model.JournalArticleDTO" %>
<%@ page import="ru.news.service.Impl.JournalArticleCustomServiceImpl" %>
<%@ page import="ru.news.service.LocalisationService" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="liferay-portlet" uri="http://liferay.com/tld/portlet" %>

<%@include file="init.jsp" %>
<%
    JournalArticleCustomServiceImpl journalArticleService = new JournalArticleCustomServiceImpl();
    List<JournalArticleDTO> newsList = journalArticleService.getJournalArticlesLatestVersion();
    LocalisationService.localize(newsList, user.getLocale());
%>

<liferay-portlet:renderURL varImpl="iteratorURL">
    <portlet:param name="mvcPath" value="view.jsp"/>
</liferay-portlet:renderURL>

<aui:form>
    <liferay-ui:search-container delta="5" emptyResultsMessage="No records available"
                                 displayTerms="<%= new DisplayTerms(renderRequest) %>" iteratorURL="<%=iteratorURL %>">
        <liferay-ui:search-form page="/WEB-INF/jsp/newsblock-mvcportlet/search.jsp"
                                servletContext="<%= application %>"/>

        <liferay-ui:search-container-results
                results="<%= ListUtil.subList(newsList, searchContainer.getStart(), searchContainer.getEnd()) %>"
                total="<%= newsList.size() %>"/>

        <liferay-ui:search-container-row className="ru.news.model.JournalArticleDTO" keyProperty="articleId"
                                         modelVar="news">

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

<%--@elvariable id="tag" type="java.lang.String"--%>
<%--
<portlet:renderURL var="getNewsByTag" windowState="normal">
    <portlet:param name="action" value="renderTagView"/>
    <portlet:param name="tag" value="${tag}"/>
</portlet:renderURL>

--%>

<%--<c:if test="${not empty tag}">
    <c:out value="Search by tag: "/>
    <a href="<%=getNewsByTag%>"><b><c:out value="${tag}"/></b></a>
    <br>
    <br>
</c:if>

&lt;%&ndash;@elvariable id="category" type="java.lang.String"&ndash;%&gt;
<portlet:renderURL var="getNewsByCategory" windowState="normal">
    <portlet:param name="action" value="renderCategoryView"/>
    <portlet:param name="category" value="${category}"/>
</portlet:renderURL>

<c:if test="${not empty category}">
    <c:out value="Search by category: "/>
    <a href="<%=getNewsByCategory%>"><b><c:out value="${category}"/></b></a>
    <br>
    <br>
</c:if>--%>

<%--@elvariable id="newsList" type="java.util.List"--%>
<%--
<c:forEach var="news" items="${newsList}" varStatus="loop">

    <portlet:renderURL var="getViewNewsURL" windowState="normal">
        <portlet:param name="action" value="renderSingleNews"/>
        <portlet:param name="groupId" value="${news.groupId}"/>
        <portlet:param name="articleId" value="${news.articleId}"/>
    </portlet:renderURL>

    <a href="<%=getViewNewsURL%>"><b><c:out value="${news.title}"/></b></a>

    <c:out value="${news.publishDate}"/>
    <br>
    <c:out value="${news.content}" escapeXml="false"/>

    <c:if test="${loop.index + 1 != fn:length(newsList)}">
        <br><br>
    </c:if>
</c:forEach>

--%>
