<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.website.auth.Authentication, java.util.*, quiz.friends.*, quiz.messaging.*" %>

<%
if(!Authentication.require_login(request, response)) return;
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
%>

<ex:push key="body.content">
	<style>
		tab {
			text-indent: 2.0em;
		}
		half-tab{
			text-indent: 0.4em;
		}
	</style>
	<h3 style="text-align:left;float:left;"><a href="messaging/messages.jsp">Messages</a> &bull; 
	    <span style="font-weight:normal;"><a href="messaging/friendRequests.jsp"">Friend Requests</a></span>	    
	</h3>
	<div style="float:right;padding-left:15px;padding-top:5px">
		<form method="post" action="messaging/compose.jsp">
			<input type="submit" style="margin:15px;font-size:17px;font-weight:bold" value="Compose" />
		</form>
	</div>
	
	<hr style="clear:both;"/>
	<table cellpadding="3" cellspacing="3" border="0" width="100%">
		<tbody>
			<%
			int i = 1;
			ArrayList<Message> messages = (ArrayList<Message>)MessageManager.getMessages(currentUser.user_id);
			if (messages.isEmpty()) {
				%><center><h4> No new mail! </h4></center><% 
			} else {
				for(Message m : messages) {
					User sender = User.getUser(m.sender_user_id);
				%>
				<tr>
					<td align="left"><input type="checkbox" name="delete_msg" value="<%=m.message_id%>" ></td>
					<td align="left"><a href=""><%=sender.name%></a></td>
					<td align="left"><a href=""><%=m.subject%></a></td>
					<td align="left"><%=sdf.format(m.time_sent)%></td>
				</tr>
				<%		
					}
				 %>
		</tbody>
	</table>
			<br>
			<form method="post" action="MessageServlet">
					<!-- <half-tab><div><input type="checkbox" name="all"></half-tab><tab>Check All</tab> -->
					<div> Select:&nbsp;&nbsp;<a href="MessageServlet">All</a>&nbsp;&nbsp;|&nbsp;&nbsp;None
					<span style="float:right"><b>Selected Messages :&nbsp;&nbsp;&nbsp;</b><select name="type">
						<option value="delete">Delete Messages</option>
						<option value="read">Mark as Read</option>
						<option value="unread">Mark as Unread</option>
					</select>
						<input type="hidden" name="quiz_id" value="s">
						<input type="submit" value="Update"></span>
					</div>
			</form>
		<%		
			}
		%>

	
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">MailBox</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>