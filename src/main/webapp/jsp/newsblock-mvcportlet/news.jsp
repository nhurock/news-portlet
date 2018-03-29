<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@include file="init.jsp" %>

<a href="<portlet:renderURL />">&laquo; <liferay-ui:message key="portlet.navigation.label.home"/> </a>

<b><%--@elvariable id="news" type="ru.news.model.JournalArticleDTO"--%>
    <c:out value="${news.title}"/></b>
    <c:out value="${news.publishDate}"/>
<br>
    <c:out value="${news.content}" escapeXml="false"/>

<c:if test="${not empty news.category}">
    <br>
    <br>
    <c:out value="categories: "> </c:out>
    <c:forEach var="category" items="${news.category}" varStatus="loop">
        <c:out value="${category}"/>
        <%-- <c:if test="${loop.index + 1 != fn:length(news.category)}">
             <c:out value=","> </c:out>
         </c:if>--%>
    </c:forEach>
</c:if>

<c:if test="${not empty news.tags}">
    <br>
    <br>
    <c:out value="tags: "> </c:out>
    <c:forEach var="tag" items="${news.tags}" varStatus="loop">
        <c:out value="${tag}"/>
        <%--<c:if test="${loop.index + 1 != fn:length(news.tags)}">
            <c:out value=","> </c:out>
        </c:if>--%>
    </c:forEach>
</c:if>




