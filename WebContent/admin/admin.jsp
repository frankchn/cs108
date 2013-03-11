<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication, quiz.friends.*, quiz.messaging.*" %>

<%
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
int numQuizzes = QuizManager.getNumQuizzes();
int numUsers = UserManager.numAllUsers();
int numTags = QuizManager.getNumTags();
int numQuizzesTaken = QuizManager.getNumTaken();

int numFriends = RelationManager.numFriendships();
int numMsgsSent = MessageManager.numAllMsgs();
int numChallenges = MessageManager.numAllChallenges();
int numFriendReqs = MessageManager.numAllFriendReqs();
%>

<ex:push key="body.content">
	<style type="text/css">
		div.inline {float:left;}
		.clearBoth {clear:both;}
	</style>
	<h2><a href="index.jsp">Announcements</a>
	<% if(currentUser != null && currentUser.is_admin) { %>
	&bull; Admin
	<% } %></h2><hr>
		<a href="table" style="padding-left:20px">Manage Users |</a>		
		<a href="table">Manage Quizzes</a><br>
	
	<br>

		<div class ="inline" style="padding-left:20px;padding-right:20px">
			<div style="border-top-width:1px;border-top-style:solid;border-top-color:#d8d8d8;padding:1.5px;margin-bottom:0px;background-color:#ececec">
				<b><font size="3px">&nbsp;&nbsp;Activity Summary</font></b>
			</div>
				<table style="width:435px;margin-top:4px">
					<tr>
						<td align="left" width="47%" style="border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#d8d8d8;"><font color="#000000">&nbsp;&nbsp;<i>Content</i></font></td>
						<td align="left" width="6%"></td>
						<td align="left" width="47%" style="border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#d8d8d8;"><font color="#000000">&nbsp;&nbsp;<i>Social Activity</i></font></td>
					</tr>
				</table>
			
			<div style="width:425px;">
			<table cellpadding="5" cellspacing="5" border="0" width="100%">
				<tbody>
					<tr>
						<td align="right" width=7%><b><%=numUsers %> </b></td>
						<td align="left" width="38%"><font color="#000000">Users</font></td>
						<td align="left" width="8%"></td>
						<td align="right" width=9%><b><%=numFriends %></b></td>
						<td align="left" width="38%"><font color="#000000">Friendships</font></td>
					</tr>	
					<tr>
						<td align="right" width=7%><b><%=numQuizzes %> </b></td>
						<td align="left" width="38%"><font color="#000000">Quizzes</font></td>
						<td align="left" width="8%"></td>
						<td align="right" width=9%><b><%=numFriendReqs %></b></td>
						<td align="left" width="38%"><font color="#000000">Friend Requests</font></td>
					</tr>
					<tr>
						<td align="right" width=7%><b><%=numQuizzesTaken%> </b></td>
						<td align="left" width="38%"><font color="#000000">Quiz Attempts</font></td>
						<td align="left" width="8%"></td>
						<td align="right" width=9%><b><%=numMsgsSent %></b></td>
						<td align="left" width="38%"><font color="#000000">Messages Sent</font></td>
					</tr>
					<tr>
						<td align="right" width=7%><b><%=numTags %> </b></td>
						<td align="left" widtsh="38%"><font color="#000000">Quiz Tags</font></td>
						<td align="left" width="8%"></td>
						<td align="right" width=9%><b><%=numChallenges %></b></td>
						<td align="left" width="38%"><font color="#000000">Challenges</font></td>
					</tr>
				</tbody>
			</table>
			</div>
		</div>

		<div style="float:right;padding-left:20px;padding-right:20px">
			<!--<div style="padding:5px;margin-bottom:10px;background-color:#e2e2e2">-->
			<div style="border-top-width:1px;border-top-style:solid;border-top-color:#d8d8d8;padding:2px;margin-bottom:10px;background-color:#ececec">
			<b><font size="3px">&nbsp;&nbsp;Post a New Announcement</font></b>
			</div>
			<form action="admin/NewAnnouncementServlet" method="post">
				<div style="padding-left:10px;padding-right:10px"><input placeholder="Enter Title Here" style="width:405px;font-size:15px;margin-bottom:10px" type="text" name="subject" /></div>
				<div style="padding-left:10px;padding-right:10px"><textarea placeholder="Enter Announcement Text Here" style="width:405px;height:100px;" name="body"></textarea></div>
				<div style="padding-left:10px;padding-right:10px"><input type="submit" value="Post Announcement!" /></div>
			</form>
		</div>
		<br class="clearBoth"/>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Dashboard</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>