<%@ taglib prefix="liferay-ui" uri="http://liferay.com/tld/ui" %>
<%@ taglib prefix="aui" uri="http://alloy.liferay.com/tld/aui" %>
<%@ page import="com.liferay.portal.kernel.dao.search.SearchContainer" %>
<%@ page import="com.liferay.portal.kernel.dao.search.DisplayTerms" %>

<%@include file="init.jsp" %>

<%
    SearchContainer searchContainer = (SearchContainer)request.getAttribute("liferay-ui:search:searchContainer");
    DisplayTerms displayTerms = searchContainer.getDisplayTerms();
%>

<liferay-ui:search-toggle buttonLabel="News Search" displayTerms="<%= displayTerms %>" id="toggle_id_news_search">
    <aui:input name="title" label="Title" value="<%=title %>" />
    <%--<aui:input name="category" label="Categroy" value="category" />
    <aui:input name="tag" label="Tag" value="tag" />--%>
</liferay-ui:search-toggle>