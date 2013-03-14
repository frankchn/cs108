<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication, quiz.friends.*, quiz.messaging.*, java.util.*" %>

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

String user_search = request.getParameter("user_search");
String quiz_search = request.getParameter("quiz_search");


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
		<a href="admin/admin.jsp#edit_user" style="padding-left:20px">Manage Users |</a>		
		<a href="admin/admin.jsp#edit_quiz">Manage Quizzes</a><br>
	
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
						<td align="left" width="38%"><font color="#000000">Quiz Tags</font></td>
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
		<a name="edit_user"></a>
		
		<br>
		<div style="padding-left:20px;padding-right:20px">
			<div style="border-top-width:1px;border-top-style:solid;border-top-color:#d8d8d8;padding:2px;margin-bottom:10px;background-color:#ececec">
				<b><font size="3px">&nbsp;&nbsp;Quick User Edit</font></b>
			</div>
			<div style="padding-left:10px;padding-right:10px;margin-bottom:15px;margin-top:15px; position:relative; left:-1px;">
				<form style="display: inline;" action="admin/admin.jsp#edit_user" method="post">
					<input type="text" name="user_search" placeholder="Search by email or name" size="45"> 
					<input type="submit" name="search" value="Search">
				</form>
			</div>
			<div style="margin-bottom:10px;margin-right:10px;margin-left:10px;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#d8d8d8;">
				<span style="width:35%;display:inline-block"><b>User</b></span>
				<span style="width:35%;display:inline-block"><b>Email</b></span>
				<span style="width:18%;display:inline-block"><b>Status</b></span>
				<span style="display:inline-block"><b>Actions</b></span>
			</div>
			<% 
			ArrayList<User> defaultUsers;
			if (user_search == null || user_search.isEmpty() || user_search.equals("null")) {
				defaultUsers = UserManager.getAdminUsers();
			} else {
				defaultUsers = UserManager.search(user_search);
			}
			
			if (defaultUsers.size() != 0) { 
				for (int i = 0; i < defaultUsers.size(); i++) { 
					User u = defaultUsers.get(i);
					if (u.user_id != currentUser.user_id) {
				%>
					<div style="margin-bottom:10px;margin-right:10px;margin-left:10px;">
					<div class="search_user_right">
						<div>
							<form style="display:inline" action="AdminServlet" method="POST">
								<%if (!u.is_admin) { 
								%>
								<input type="submit" name="admin_promote" value="Promote">	
								<%} else { 
								%>
								<input type="submit" name="admin_demote" value="Demote">
								<%} %>
								&nbsp; <input type="submit" name="admin_delete" value = "Delete User">				
								<input type="hidden" name="user_id" value=<%=u.user_id%>>
								<input type="hidden" name="search_query" value=<%=user_search%>>
							</form>
						</div>
					</div>
					<div class="search_user_left">
						<div align="left">
						<span style="width:35%;display:inline-block"><a href="user/profile.jsp?user=<%=u.user_id%>"><%=u.name %></a></span>
						<span style="width:35%;display:inline-block"><%=u.email%></span>
						<%if (u.is_admin) { %>
							<span style="width:10%;display:inline-block">ADMIN</span>
						<%} else { %>
							<span style="width:10%;display:inline-block"></span>
						<%} %>
						</div>
					</div>
				</div>
			<%		}
				}
			} else {
			%>
		</div>
		<div class="no_results" style="margin-bottom:10px;margin-right:10px;margin-left:10px;">
			No Users Matched.
		</div>
			<%	
			}
			%>
		
		<br>
		<a name="edit_quiz"></a>
		
		<div style="padding-left:0px;padding-right:0px">
			<div style="border-top-width:1px;border-top-style:solid;border-top-color:#d8d8d8;padding:2px;margin-bottom:10px;background-color:#ececec">
				<b><font size="3px">&nbsp;&nbsp;Quick Quiz Edit</font></b>
			</div>
			<div style="padding-left:10px;padding-right:10px;margin-bottom:15px;margin-top:15px;position:relative; left:-1px;">
				<form style="display: inline;" action="admin/admin.jsp#edit_quiz" method="post">
					<input type="text" name="quiz_search" placeholder="Search by quiz name or description" size="45"> 
					<input type="submit" name="search" value="Search">
				</form>
			</div>
			<div style="margin-bottom:10px;margin-right:10px;margin-left:10px;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:#d8d8d8;">
				<span style="width:32%;display:inline-block"><b>Quiz</b></span>
				<span style="width:27%;display:inline-block"><b>Creator</b></span>
				<span style="width:22%;display:inline-block"><b>Date Created</b></span>
				<span style="display:inline-block"><b>Actions</b></span>
			</div>
			<% ArrayList<Quiz> editQuizzes;
				if (quiz_search == null || quiz_search.isEmpty() || quiz_search.equals("null")) {
					editQuizzes = QuizManager.getRecentQuizzes();
				} else {
					editQuizzes = QuizManager.searchQuizzes(quiz_search);
				}
			
			if (editQuizzes.size() != 0) { 
				for (int i = 0; i < editQuizzes.size(); i++) { 
					Quiz q = editQuizzes.get(i);
				%>
					<div style="margin-bottom:10px;margin-right:10px;margin-left:10px;">
					<div class="search_user_right">
						<div>
							<form style="display:inline" action="AdminServlet" method="POST">
								<input type="submit" name="clear_history" value="Clear History">	
								&nbsp; <input type="submit" name="quiz_delete" value = "Delete">				
								<input type="hidden" name="quiz_id" value=<%=q.quiz_id%>>
								<input type="hidden" name="search_query" value=<%=quiz_search%>>
							</form>
						</div>
					</div>
					<div class="search_user_left">
						<div align="left">
						<span style="width:32%;display:inline-block"><a href="quiz/info.jsp?quiz_id=<%=q.quiz_id%>"><%=q.name %></a></span>
						<span style="width:27%;display:inline-block"><%=(User.getUser(q.user_id)).name%></span>
						<span style="width:22%;display:inline-block"><%=sdf.format(q.created)%></span>
						</div>
					</div>
				</div>
			<%		
				}
			} else {
			%>
		</div>
		<div class="no_results" style="margin-bottom:10px;margin-right:10px;margin-left:10px;">
			No Quizzes Matched.
		</div>
			<%	
			}
			%>
		</div><br>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Dashboard</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>