<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://alloy.liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="theme" %>

<%@ page import="com.liferay.portal.kernel.exception.PortalException" %>

<%@ page import="com.liferay.portal.kernel.exception.SystemException" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portal.model.User" %>
<%@ page import="com.liferay.portal.util.PortalUtil" %>
<%@ page import="java.util.List" %>

<portlet:defineObjects/>
<theme:defineObjects/>

<%
    String currentURL = PortalUtil.getCurrentURL(request);
    User currentUser = null;
    try {
        currentUser = PortalUtil.getUser(request);
    } catch (PortalException | SystemException e) {
        e.printStackTrace();
    }
    String title = ParamUtil.getString(request, "title");
%>