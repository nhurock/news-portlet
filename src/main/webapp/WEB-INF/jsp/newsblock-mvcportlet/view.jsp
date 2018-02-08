<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>
<%@include file="init.jsp" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--@elvariable id="tag" type="java.lang.String"--%>
<portlet:renderURL var="getNewsByTag" windowState="normal">
    <portlet:param name="action" value="renderTagView"/>
    <portlet:param name="tag" value="${tag}"/>
</portlet:renderURL>


<c:if test="${not empty tag}">
    <c:out value="Search by tag: "/>
    <a href="<%=getNewsByTag%>"><b><c:out value="${tag}"/></b></a>
    <br>
    <br>
</c:if>

<%--@elvariable id="category" type="java.lang.String"--%>
<portlet:renderURL var="getNewsByCategory" windowState="normal">
    <portlet:param name="action" value="renderCategoryView"/>
    <portlet:param name="category" value="${category}"/>
</portlet:renderURL>

<c:if test="${not empty category}">
    <c:out value="Search by category: "/>
    <a href="<%=getNewsByCategory%>"><b><c:out value="${category}"/></b></a>
    <br>
    <br>
</c:if>


<%--@elvariable id="newsList" type="java.util.List"--%>
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

