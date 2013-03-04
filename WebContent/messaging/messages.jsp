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
	<form method="post" action="MessageServlet">
	<fieldset style="border:0 none;">
	<table cellpadding="5" cellspacing="5" border="0" width="100%">
		<tbody>
			<%
			ArrayList<Message> messages = (ArrayList<Message>)MessageManager.getMessages(currentUser.user_id);
			if (messages.isEmpty()) {
				%><center><h4> No new mail! </h4></center><% 
			} else {
				
				for(int i = 0; i < messages.size(); i++) {
					Message m = messages.get(i);
					User sender = User.getUser(m.sender_user_id);
				%>
				<tr>
					<td align="left" width="7%" >
					<input type="checkbox" id="checkbox<%=i%>" name="check" value="<%=m.message_id%>"></td>
					<%if (m.unread) { %>
					<td align="left" width="22%"><a href="user/profile.jsp?user=<%=sender.user_id %>"><font color="#000000"><b><%=sender.name%></b></font></a></td>
					<td align="left" width="49%"><a href="messaging/readMsgs.jsp?msg_id=<%=m.message_id%>"><font color="#000000"><b><%=m.subject%></b></font></a></td>
					<td align="right" width="20%"><b><%=sdf.format(m.time_sent)%></b></td>
					<%} else {%>
						<td align="left" width="22%"><a href="user/profile.jsp?user=<%=sender.user_id %>"><font color="#000000"><%=sender.name%></font></a></td>		
						<td align="left" width="49%"><a href="messaging/readMsgs.jsp?msg_id=<%=m.message_id%>"><font color="#000000"><%=m.subject%></font></a></td>
						<td align="right" width="20%"><%=sdf.format(m.time_sent)%></td>
					<%}%>
				</tr>	
			<% } %>
		</tbody>
	</table>
	</fieldset>
	
			<hr>
					<div> Select:&nbsp;&nbsp;<a href="javascript:;" onclick="checkAll(this)" >All</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:;" id="checknone">None</a>
					<span style="float:right"><b>Selected Messages :&nbsp;&nbsp;&nbsp;</b>
					<select name="type">
						<option value="delete">Delete Messages</option>
						<option value="read">Mark as Read</option>
						<option value="unread">Mark as Unread</option>
					</select>
						<input type="hidden" name="quiz_id" value="s">
						<input type="submit" value="Update"></span>
					</div>
		<%		
			}
		%>
	</form>
	<script type="text/javascript">
		function checkAll(link) {
			var inputs = document.getElementsByTagName('input');
			for (var i=0; i < inputs.length; i++) {
				if (inputs[i].type == 'checkbox') {
					inputs[i].checked = true;
				}
			}
		}

		$(function () {
			$('#checknone').click(function () {
    			$('fieldset').find(':checkbox').attr('checked', false);
    			return true;
			});
		});
	</script>
	
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">MailBox</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>