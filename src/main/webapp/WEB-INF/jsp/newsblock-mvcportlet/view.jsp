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
<%@include file="init.jsp"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--This is the <b>newsblock-mvcportlet</b> portlet.<br />--%>
<%--<c:out escapeXml="true" value="${releaseInfo}" />.--%>

<%--<c:out escapeXml="true" value="${newsList}" />.--%>

<%--<jsp:useBean id="newsList" scope="request" type="java.util.List"/>--%>



<c:forEach var="news" items="${newsList}" varStatus="loop">

    <portlet:renderURL  var="getViewNewsURL" windowState="normal">
        <portlet:param name="action" value="renderOne"/>
        <portlet:param name="groupId" value="${news.groupId}"/>
        <portlet:param name="articleId" value="${news.articleId}"/>
    </portlet:renderURL>

    <a href="<%=getViewNewsURL%>"><b><c:out value="${news.title}"/></b></a>

    <c:out value="${news.date}"/>
    <br>
    <c:out value="${news.content}"  escapeXml="false"/>

    <c:if test="${loop.index + 1 != fn:length(newsList)}">
        <br><br>
    </c:if>
    
</c:forEach>

