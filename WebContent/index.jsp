<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>

<ex:push key="body.content">
	<h1>Announcements</h1>
	<p>This is a test announcement by someone.</p>
	<%= "We Love SCRIPTLET!" %>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Welcome!</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>