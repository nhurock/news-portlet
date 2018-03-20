<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>
<%@ page import="com.liferay.portal.util.PortalUtil" %>

<portlet:defineObjects/>
<theme:defineObjects/>

<%
    String currentURL = PortalUtil.getCurrentURL(request);
%>