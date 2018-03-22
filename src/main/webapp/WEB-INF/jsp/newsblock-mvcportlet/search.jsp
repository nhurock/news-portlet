<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ taglib prefix="aui" uri="http://alloy.liferay.com/tld/aui" %>
<%@ page import="ru.news.search.JournalArticleDTODisplayTerms" %>
<%@ page import="ru.news.search.JournalArticleDTOSearchContainer" %>

<%@include file="init.jsp" %>

<%
    JournalArticleDTOSearchContainer searchContainer = (JournalArticleDTOSearchContainer) request.getAttribute("liferay-ui:search:searchContainer");
    JournalArticleDTODisplayTerms displayTerms = (JournalArticleDTODisplayTerms) searchContainer.getDisplayTerms();

/*    String showArchiveNews = portletPreferences.getValue("enableArchiveNews", "");
    Boolean showArchiveNewsFlag;
    showArchiveNewsFlag = Objects.equals(showArchiveNews, "on");
    displayTerms.setEnableArchiveNews(showArchiveNewsFlag);*/
%>

<liferay-ui:search-toggle buttonLabel="News Search" displayTerms="<%= displayTerms %>" id="toggle_id_news_search">
    <aui:input label="Title" name="title" value="<%=displayTerms.getTitle() %>"/>
    <aui:input label="Tag" name="tag" value="<%=displayTerms.getTag() %>"/>
    <aui:input label="Categroy" name="category" value="<%=displayTerms.getCategory() %>"/>
</liferay-ui:search-toggle>