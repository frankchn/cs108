<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>

<ex:push key="body.content">
	<% if(request.getParameter("bad") != null) { %>
	<div class="error_message">
		<strong>Wrong Credentials</strong> Sorry, you have entered either the wrong email address
		or password into our system. Please try again.
	</div>
	<% } %>
	<div>Please login with your email address and password. If this is the first
	   time you are visiting our site, please click <a href="auth/register.jsp">here</a> to register.</div>
	<form method="post" action="auth/LoginServlet">
		<table cellspacing="3" cellpadding="3" border="0">
			<tr>
				<th>Email Address</th>
				<td><input type="email" size="40" /></td>
			</tr>
			<tr>
				<th>Password</th>
				<td><input type="password" size="20" /></td>
			</tr>
			<tr>
				<th>&nbsp;</th>
				<td><input type="submit" value="Login Now"></td>
			</tr>
		</table>
	</form>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Login</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>