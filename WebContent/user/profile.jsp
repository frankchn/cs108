<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.website.auth.Authentication" %>

<%
if(!Authentication.require_login(request, response)) return;

User currentUser = (User) session.getAttribute("currentUser");

%>

<ex:push key="body.content">
	<form method="post" action="quiz/edit/EditQuizInfo">
		<table cellspacing="3" cellpadding="3" border="0">
			<tr>
				<th>Friends</th>
				<td><input name="name" type="text" size="60" value="<%=currentUser.name %>" /></td>
			</tr>
			
		</table>
	</form>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle"><%=currentUser.name %></jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>