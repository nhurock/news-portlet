<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ taglib prefix="aui" uri="http://alloy.liferay.com/tld/aui" %>
<%@ page import="ru.news.search.JournalArticleDTODisplayTerms" %>
<%@ page import="ru.news.search.JournalArticleDTOSearchContainer" %>
<%@ page import="ru.news.constant.DisplayTermsParam" %>

<%@include file="init.jsp" %>

<%
    JournalArticleDTOSearchContainer searchContainer = (JournalArticleDTOSearchContainer) request.getAttribute("liferay-ui:search:searchContainer");
    JournalArticleDTODisplayTerms displayTerms = (JournalArticleDTODisplayTerms) searchContainer.getDisplayTerms();
%>

<liferay-ui:search-toggle buttonLabel="search-toggle.label.search-button.title" displayTerms="<%=displayTerms %>"
                          id="toggle_id_news_search">
    <aui:input label="search-toggle.label.title" name="<%=DisplayTermsParam.TITLE.getName() %>"
               value="<%=displayTerms.getTitle() %>"/>
    <aui:input label="search-toggle.label.tag" name="<%=DisplayTermsParam.TAG.getName() %>"
               value="<%=displayTerms.getTag() %>"/>
    <aui:input label="search-toggle.label.category" name="<%=DisplayTermsParam.CATEGORY.getName() %>"
               value="<%=displayTerms.getCategory() %>"/>
</liferay-ui:search-toggle>