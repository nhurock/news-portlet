<%@ page import="java.util.Objects" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib prefix="aui" uri="http://alloy.liferay.com/tld/aui" %>

<liferay-portlet:actionURL var="configurationURL" portletConfiguration="true"/>
<portlet:defineObjects/>
<%
    String showArchiveNews = portletPreferences.getValue("enableArchiveNews", "");
    Boolean showArchiveNewsFlag = Objects.equals(showArchiveNews, "on");
%>

<liferay-ui:success key="config-stored" message="Configuration Saved Successfully"/>

<form method="post" action="<%=configurationURL%>">
    <label>
        View Archive news:

        <c:if test="<%= showArchiveNewsFlag%>">
            <input class="enableArchiveNews" id="enableArchiveNewsId" type="checkbox" checked=""
                   name='<portlet:namespace/>enableArchiveNews'>
        </c:if>

        <c:if test="<%=!showArchiveNewsFlag%>">
            <input class="enableArchiveNews" id="enableArchiveNewsId" type="checkbox"
                   name='<portlet:namespace/>enableArchiveNews'>
        </c:if>

    </label>
    <input type="submit" value="Save">
</form>

