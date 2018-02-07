<%--
  Created by IntelliJ IDEA.
  User: pushkin
  Date: 31.01.2018
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@include file="init.jsp"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<b><c:out value="${news.title}"/></b>
<c:out value="${news.date}"/>
<br>
<c:out value="${news.content}"  escapeXml="false"/>
