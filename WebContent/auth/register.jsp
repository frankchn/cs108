<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>

<ex:push key="body.content">
	<% if(request.getParameter("bad_email") != null) { %>
	<div class="error_message">
		<strong>Account Exists</strong> Sorry, an account with the email address you
		have specified already exists. Please try again with another email.
	</div>
	<% } %>
	<% if(request.getParameter("missing") != null) { %>
	<div class="error_message">
		<strong>Information Missing</strong> Sorry, some required information is missing.
		Please try again.
	</div>
	<% } %>
	<div>Fill in the form below to create a new account.</div>
	<form method="post" action="auth/RegisterServlet">
		<table cellspacing="3" cellpadding="3" border="0">
			<tr>
				<th>Email Address</th>
				<td><input type="email" name="email" size="40" /></td>
			</tr>
			<tr>
				<th>Password</th>
				<td><input type="password" name="password" size="20" /></td>
			</tr>
			<tr>
				<th>Full Name</th>
				<td><input type="full_name" name="full_name" size="40" /></td>
			</tr>
			<tr>
				<th>&nbsp;</th>
				<td><input type="submit" value="Register Now"></td>
			</tr>
		</table>
	</form>
</ex:push>


<t:standard>
    <jsp:attribute name="pageTitle">Registration</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>