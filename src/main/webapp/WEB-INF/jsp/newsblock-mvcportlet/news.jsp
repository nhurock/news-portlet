<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@include file="init.jsp" %>

<a href="<portlet:renderURL />">&laquo;Home</a>
<div class="separator"></div>

<c:out value="<%=currentUser.getLanguageId() %>"/>
<br>


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

        <portlet:renderURL var="getNewsByCategory" windowState="normal">
            <portlet:param name="action" value="renderCategoryView"/>
            <portlet:param name="category" value="${category}"/>
        </portlet:renderURL>

        <a href="<%=getNewsByCategory%>"><b><c:out value="${category}"/></b></a>
        <%-- <c:if test="${loop.index + 1 != fn:length(news.category)}">
             <c:out value=","> </c:out>
         </c:if>--%>
    </c:forEach>
    <br>
    <br>
</c:if>


<c:if test="${not empty news.tags}">
    <c:out value="tags: "> </c:out>
    <c:forEach var="tag" items="${news.tags}" varStatus="loop">

        <portlet:renderURL var="getNewsByTag" windowState="normal">
            <portlet:param name="action" value="renderTagView"/>
            <portlet:param name="tag" value="${tag}"/>
        </portlet:renderURL>

        <a href="<%=getNewsByTag%>"><b><c:out value="${tag}"/></b></a>
        <%--<c:if test="${loop.index + 1 != fn:length(news.tags)}">
            <c:out value=","> </c:out>
        </c:if>--%>
    </c:forEach>
</c:if>




