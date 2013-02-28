<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.website.auth.Authentication" %>

<%
if(!Authentication.require_login(request, response)) return;

User currentUser = (User) session.getAttribute("currentUser");

%>

<ex:push key="body.content">
	<h2>Friends</h2>
	<h2>Achievements</h2>
		<div class="achievement_grid">
		<% for (int i = 0; i < 9; i++) { %>
			<p>hello</p>
		<%} %>
		</div>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle"><%=currentUser.name %></jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>