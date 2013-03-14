<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication, java.util.*" %>

<%
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
%>

<ex:push key="body.content">
	<h2>Announcements
	<% if(currentUser != null && currentUser.is_admin) { %>
	&bull; <a href="admin/admin.jsp">Admin</a>
	<% } %></h2>
	<% for(Announcement a : Announcement.getAnnouncements()) { %>
	<div style="padding-left:20px;padding-right:20px;padding-bottom:5px;border-bottom:1px solid #ccc">
		<h4><%=a.subject %></h4>
		<div><%=a.body %></div>
		<div style="text-align:right">
			<em>
				Posted by <a href="user/profile.jsp?user=<%=a.user_id%>"><%=User.getUser(a.user_id).name %></a> on <%=sdf.format(a.posted) %>
				<% if(currentUser != null && currentUser.is_admin) { %>
				&bull; <a href="admin/DeleteAnnouncementServlet?announcement_id=<%=a.announcement_id %>">Delete</a>
				<% } %>
			</em>
		</div>
	</div>
	<% } %>
	<style>
	tab {
		text-indent: 2.0em;
	}
	</style>
<%if (currentUser != null) { %>
	<h2> My Friends' Activities</h2>
	<tab><div>
	<% List<Activity> acts = currentUser.getFriendsActivity();
	int size = 10;
	if (acts.size() < 10) {
		size = acts.size();
	}
	for (int a = 0; a < size; a++) {
		Activity act = acts.get(a);%>
		<p><a href="user/profile.jsp?user=<%=act.user_id%>"><%= User.getUser(act.user_id).name %></a> <%= act.description %>
		<% if (act.quiz != null) { %>
			&nbsp;<a href="quiz/info.jsp?quiz_id=<%=act.quiz.quiz_id%>"><%= act.quiz.name %></a>
		<%} else {%>
			<a href="user/profile.jsp?user=<%=act.user_id %>#ach"><%= act.achievement %></a>
		<%} %>
		</p>
	<%} %>
	</div></tab>
	<h2>Most Popular Quizzes</h2>
	<table cellpadding="3" cellspacing="3" border="0">
	<tr>
		<th>Quiz Name</th>
		<th>Rating</th>
	</tr>
	<% Quiz[] top_five = Quiz.getTopFive(); 
		for (int tf = 0; tf < top_five.length; tf++) {%>
	<tr>
		<td align="left"><a href="quiz/info.jsp?quiz_id=<%=top_five[tf].quiz_id%>"><%=top_five[tf].abbrev_name %></a></td>
		<td align="center"><%=top_five[tf].cur_rating %></td>
	</tr>
	<%
	}
	%>
</table>
	<h2>Recently Created Quizzes</h2>
	<table cellpadding="3" cellspacing="3" border="0">
	<tr>
		<th>Quiz Name</th>
		<th>Date Created</th>
	</tr>
	<% Quiz[] recent_quizzes = Quiz.getRecent(); 
		for (int rq = 0; rq < recent_quizzes.length; rq++) {%>
	<tr>
		<td align="left"><a href="quiz/info.jsp?quiz_id=<%=recent_quizzes[rq].quiz_id%>"><%=recent_quizzes[rq].abbrev_name %></a></td>
		<td align="center"><%=sdf.format(recent_quizzes[rq].created) %></td>
	</tr>
	<%
	}
	%>
</table>	
	<h2>My Quizzes</h2>
	<table cellpadding="3" cellspacing="3" border="0">
	<tr>
		<th>Quiz Name</th>
		<th>Date Created</th>
	</tr>
	<% Quiz[] my_quizzes = Quiz.getMyRecentCreated(currentUser.user_id); 
		for (int mq = 0; mq < my_quizzes.length; mq++) {%>
	<tr>
		<td align="left"><a href="quiz/info.jsp?quiz_id=<%=my_quizzes[mq].quiz_id%>"><%=my_quizzes[mq].abbrev_name %></a></td>
		<td align="center"><%=sdf.format(my_quizzes[mq].created) %></td>
	</tr>
	<%
	}
	%>
</table>
	<h2>My Attempts Today</h2>
	<table cellpadding="3" cellspacing="3" border="0">
	<tr>
		<th>Quiz Name</th>
		<th>Started</th>
		<th>Completed</th>
		<th>Score</th>
		<th>Action</th>
	</tr>
	<% QuizAttempt[] qas = QuizAttempt.last_day_attempt(currentUser.user_id); 
	int counter = qas.length;
	for(QuizAttempt qa : qas) {
	%>
	<tr>
		<td align="left"><a href="quiz/info.jsp?quiz_id=<%=qa.quiz_id%>"><%=Quiz.getQuiz(qa.quiz_id).abbrev_name %></a></td>
		<td align="center"><%=sdf.format(qa.start_time) %></td>
		<td align="center"><%=qa.submission_time != null ? sdf.format(qa.submission_time) : "Not complete" %></td>
		<td align="center"><%=qa.finished ? (!qa.show_score ? "Practice" : qa.score) : "Not complete" %></td>
		<td align="center"><%=qa.finished ? 
				"<a href='quiz/attempt/results.jsp?quiz_attempt_id=" + qa.quiz_attempt_id + "'>View Results</a>" : 
				"<a href='quiz/attempt/attempt.jsp?quiz_attempt_id=" + qa.quiz_attempt_id + "'>Continue</a>" %></td>
	</tr>
	<%
	}
	%>
</table>
	<h2>Achievements Earned</h2>
	<tab>
	<div class="achievement_grid">
		<%
		List<Achievement> achievements = currentUser.getAchievements();
		for (int i = 0; i < achievements.size(); i++) {
		%>
		<img style="margin-right: 20px" src="<%=achievements.get(i).img%>"
		title="<%=currentUser.name%><%=achievements.get(i).description%>">
		<%
		}
		%>
	</div>
	</tab>
<%} %> <!-- end if currentUser != null -->
	<script type="text/javascript">
	
	function showNewAnnouncementForm() {
		$(".newannouncement").show();
	}
	
	</script>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Welcome!</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>