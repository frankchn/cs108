<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication, java.util.*" %>

<%
if(!Authentication.require_login(request, response)) return;

User currentUser = (User) session.getAttribute("currentUser");
ArrayList<User> friends = (ArrayList<User>)currentUser.getFriends();
%>

<ex:push key="body.content">
<h3>Compose New Message</h3>
	<div><form method="post" action="MessageServlet">
		<table cellspacing="3" cellpadding="3" border="0">
			<tr>
				<th align="left" width="10%">To </th>
				<td align="left">
					<input id="email_field" name= "email_field" type="text" style="width:300px" value="" readonly/>
					<input id="id_field" name = "id_field" type="hidden" value=""/>
					<select name="type" id="friend_dropdown">
						<option>Select a friend</option>
					<% for (int i = 0; i < friends.size(); i++) { 
						User f = friends.get(i);
					%>
						<option value="<%=f.name%> <<%=f.email%>>,<%=f.user_id%>"><%=f.email%></option>
					<%} %>
					</select>
					<script>
						var textBox = document.getElementById('email_field');
						var dropDown = document.getElementById('friend_dropdown');
						var idField = document.getElementById('id_field');
						dropDown.onchange = function() {
							var info = dropDown.value.split(",");
							textBox.value = info[0];
							idField.value = info[1];
						};
					</script>
				</td>
			</tr>
			<tr>
				<th align="left" width="10%">Subject </th>
				<td align="left"><input name="subject" type="text" style="width:500px" value=""/></td>
			</tr>
			<tr>
				<th align="left" width="10%">Message </th>
				<td><textarea name="description" style="width:500px;height:150px"></textarea></td>
			</tr>
			<tr>
				<th><input type="hidden" name="quiz_id" value=""></th>
				<td><input type="submit" name ="sendComp" value="Send">&nbsp;&nbsp;<input type="submit" value="Cancel"></td>
			</tr>
		</table>
	</form></div><br>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">MailBox</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>