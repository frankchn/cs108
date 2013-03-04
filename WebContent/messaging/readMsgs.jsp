<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.website.auth.Authentication, java.util.*, quiz.friends.*, quiz.messaging.*" %>

<%
if(!Authentication.require_login(request, response)) return;
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
int msg_id = Integer.parseInt(request.getParameter("msg_id"));
Message m = MessageManager.getMessage(msg_id);
m.markRead();
User sender = User.getUser(m.sender_user_id);
int quiz_id = -1;
Quiz challenge_quiz = null;
if (m.type.equals("CHALLENGE")) {
	quiz_id = m.quiz_id;
	challenge_quiz = Quiz.getQuiz(quiz_id);
}
%>

<ex:push key="body.content">
	<style>
		tab {
			text-indent: 2.0em;
		}
		.nudge {
			line-height: 1em;
		}
	</style>
	<h3 style="text-align:left;float:left;"><span style="font-weight:normal;"><a href="messaging/messages.jsp">Messages</a></span> &bull; 
	    <span style="font-weight:normal;"><a href="messaging/friendRequests.jsp"">Friend Requests</a> </span>
	</h3>
	<div style="float:right;padding-left:15px;padding-top:5px">
		<form method="post" action="messaging/compose.jsp">
			<input type="submit" style="margin:15px;font-size:17px;font-weight:bold" value="Compose" />
		</form>
	</div>	
	<hr style="clear:both;"/>
	<%if (quiz_id != -1) { %>
		<tab><h3><%=sender.name%> has challenged you to take <a href="quiz/info.jsp?quiz_id=<%=quiz_id%>"><%=challenge_quiz.name%></a>!</h3>
	<%} else { %>
		<tab><h3><%=m.subject %></h3>
	<%} %>
	<div>
		<table cellspacing="2" cellpadding="2" border="0">
			<tr>
				<th align="left" width="10%">From </th>
				<td align="left"><%=sender.name %></td>
			</tr>
			<tr>
				<th align="left" width="10%">Sent </th>
				<td align="left"><%=sdf.format(m.time_sent)%></td>
			</tr>
		</table>
	</div>
	<br>
	<div>
		<tab><font size="4"><textarea readonly style="width:500px;height:150px"><%=m.body%></textarea></font></tab>
	</div>
	<br>
	<form method="post" action="quiz/info.jsp?quiz_id=<%=quiz_id%>">
	<input type="submit" name ="send_compose" value="Accept the Challenge!"/>
	</form>
	</tab>	
	<br>
		
	<style>
		textarea {
			font-size:11pt;
			font-family:Helvetica;
		}
	</style>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">MailBox</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>