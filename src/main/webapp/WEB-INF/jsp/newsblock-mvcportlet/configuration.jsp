<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="aui" uri="http://alloy.liferay.com/tld/aui" %>
<liferay-portlet:actionURL var="configurationURL" portletConfiguration="true"/>
<portlet:defineObjects/>

<liferay-ui:success key="config-stored" message="Configuration Saved Successfully"/>

<div id="color" style="padding-left: 10px;"></div>

<form name="fm" method="post" action="<%=configurationURL%>">
    <label>
        View Archive news:
        <input class="enableArchiveNews" type="checkbox" name='<portlet:namespace/>enableArchiveNews'>
    </label>
    <input type="submit" value="Save">
</form>

