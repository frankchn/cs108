<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>

<ex:push key="body.content">
	<h2>Announcements</h2>
	<p>This is a test announcement by someone.</p>
	<h2>My Recent Activity</h2>
	<h2>Friends' Activities</h2>
	<h2>Popular Quizzes</h2>
	<h2>Recent Quizzes</h2>
	<h2>Achievements Earned</h2>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Welcome!</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>