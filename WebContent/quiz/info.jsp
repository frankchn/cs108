<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.*, java.text.DecimalFormat" %>

<%
if(!Authentication.require_login(request, response)) return;

User currentUser = (User) session.getAttribute("currentUser");
Quiz currentQuiz = Quiz.getQuiz(Integer.parseInt(request.getParameter("quiz_id")));

java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
DecimalFormat df = new DecimalFormat("#.###");
%>


<ex:push key="body.content">
	<h3>Quiz Information</h3>
	<table cellpadding="3" cellspacing="3" border="0">
		<tr>
			<th>Name</th>
			<td><%=currentQuiz.name %></td>
		</tr>
		<tr>
			<th>Description</th>
			<td><%=currentQuiz.description %></td>
		</tr>
		<tr>
			<th>Tags</th>
			<td><%=currentQuiz.getCurTagsAsString() %></td>
		</tr>
		<tr>
			<th>Average Rating</th>
			<td><%double rating = currentQuiz.getAvgRating();
				if (rating > 0) {%><%=rating %>
				<%} else { %>Not Yet Rated
				<% } %></td>
		</tr>
		<tr>
			<th>Created by</th>
			<td><a href="user/profile.jsp?user=<%=currentQuiz.user_id%>"><%=User.getUser(currentQuiz.user_id).name %></a></td>
		</tr>
		<tr>
			<th>Created on</th>
			<td><%=sdf.format(currentQuiz.created) %></td>
		</tr>
		<tr>
			<th>Multiple Pages</th>
			<td><%=currentQuiz.multiple_pages ? "The quiz will be rendered on multiple pages." : "The quiz will be rendered on a single page." %></td>
		</tr>
		<tr>
			<th>Random Questions</th>
			<td><%=currentQuiz.random_questions ? "The questions will be ordered randomly." : "The questions will be ordered in the order you specify." %></td>
		</tr>
		<tr>
			<th>Immediate Correction</th>
			<td><%=currentQuiz.immediate_correction ? "Individual questions will be graded immediately." : "The quiz will graded completely at the end." %></td>
		</tr>
		<tr>
			<th>Practice Mode</th>
			<td><%=currentQuiz.practice_mode ? "The quiz can be taken for practice." : "The quiz must be taken for a grade." %></td>
		</tr>
	</table>
	<div style="margin-top:20px;display:inline">
		<form method="post" action="quiz/attempt/QuizStartAttempt" style="display:inline">
			<input type="hidden" name="quiz_id" value="<%=currentQuiz.quiz_id %>">
			<input type="submit" style="font-size:16px;padding:5px;" name="real_mode" value="Take Quiz for Real">
			<% if(currentQuiz.practice_mode) { %>
			<input type="submit" style="font-size:16px;padding:5px;" name="practice_mode" value="Take Quiz without Recording Scores">
			<% } %>
		</form>	
		<% 	if(currentUser != null && (currentUser.is_admin || currentUser.user_id == currentQuiz.user_id)) { %>
		<form method="post" action="quiz/edit/index.jsp?quiz_id=<%=currentQuiz.quiz_id%>" style="display:inline">
			<input type="submit" style="font-size:16px;padding:5px;" name="edit_quiz" value="Edit Quiz">
		</form>			
		<% } %>
		<form method="post" action="messaging/compose.jsp" style="display:inline">
			<input type="hidden" name="quiz_id" value="<%=currentQuiz.quiz_id %>">
			<input type="hidden" name="high_score" value="<%=currentQuiz.getHighestScore(currentUser.user_id) %>"/>
			<input type="submit" style="font-size:16px;padding:5px" name="challenge" value="Challenge a Friend!">
		</form>
	</div>
	<h3 id= "quizStats">Quiz Statistics</h3>
	<table cellpadding="3" cellspacing="3" border="0">
		<tr>
			<th>No.Times Taken</th>
			<th>Avg. Score</th>
			<th>Std. Dev</th>
			<th>Lowest Score</th>
			<th>Highest Score</th>
		</tr>
		<% 
		int numTimes = QuizAttempt.loadNumAttempts(currentQuiz.quiz_id);
		double avgScore = QuizAttempt.loadAvg(currentQuiz.quiz_id);
		double stddevScore = QuizAttempt.loadStdDev(currentQuiz.quiz_id);
		double minScore = QuizAttempt.loadMin(currentQuiz.quiz_id);
		double maxScore = QuizAttempt.loadMax(currentQuiz.quiz_id);
		%>
		<tr>
			<td align="center"><%=numTimes%></td>
			<td align="center"><%=avgScore%></td>
			<td align="center"><%=df.format(stddevScore)%></td>
			<td align="center"><%=minScore%></td>
			<td align="center"><%=maxScore%></td>
		</tr>

	</table>
	<h3 id= "prevAttempts">My Previous Attempts</h3>
	<table cellpadding="3" cellspacing="3" border="0">
		<tr>
			<th>Attempt No.</th>
			<th>Started</th>
			<th>Completed</th>
			<th>Score</th>
			<th>Action</th>
		</tr>
		<% 
		QuizAttempt[] qas = QuizAttempt.loadAttempts(currentQuiz, currentUser);
		int counter = qas.length;
		for(QuizAttempt qa : qas) {
		%>
		<tr>
			<td align="center"><%=counter-- %></td>
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
	<h3>Today's Highest Scores</h3>
		<table cellpadding="3" cellspacing="3" border="0">
		<tr>
			<th>User</th>
			<th>Started</th>
			<th>Completed</th>
			<th>Score</th>
		</tr>
		<% 
		QuizAttempt[] ls = QuizAttempt.lastDayAttemptForQuiz(currentQuiz.quiz_id);
		for(QuizAttempt s : ls) {
		%>
		<tr>
			<td align="center"><%=User.getUser(s.user_id).name %></td>
			<td align="center"><%= sdf.format(s.start_time)%></td>
			<td align="center"><%=s.submission_time != null ? sdf.format(s.submission_time) : "Not complete" %></td>
			<td align="center"><%=s.finished ? (!s.show_score ? "Practice" : s.score) : "Not complete" %></td>
		</tr>
		<%
		}
		%>
	</table>
	<h3>Top Scores of All Time</h3>
	<table cellpadding="3" cellspacing="3" border="0">
		<tr>
			<th>User</th>
			<th>Started</th>
			<th>Completed</th>
			<th>Score</th>
		</tr>
		<% 
		QuizAttempt[] ts = QuizAttempt.loadTopScores(currentQuiz.quiz_id);
		for(QuizAttempt s : ts) {
		%>
		<tr>
			<td align="center"><%=User.getUser(s.user_id).name %></td>
			<td align="center"><%= sdf.format(s.start_time)%></td>
			<td align="center"><%=s.submission_time != null ? sdf.format(s.submission_time) : "Not complete" %></td>
			<td align="center"><%=s.finished ? (!s.show_score ? "Practice" : s.score) : "Not complete" %></td>
		</tr>
		<%
		}
		%>
	</table>
	<h3>Recent Activity</h3>
	<table cellpadding="3" cellspacing="3" border="0">
		<tr>
			<th>User</th>
			<th>Started</th>
			<th>Completed</th>
			<th>Score</th>
		</tr>
		<% 
		QuizAttempt[] hs = QuizAttempt.loadRecentScores(currentQuiz.quiz_id);
		for(QuizAttempt s : hs) {
		%>
		<tr>
			<td align="center"><%=User.getUser(s.user_id).name %></td>
			<td align="center"><%= sdf.format(s.start_time)%></td>
			<td align="center"><%=s.submission_time != null ? sdf.format(s.submission_time) : "Not complete" %></td>
			<td align="center"><%=s.finished ? (!s.show_score ? "Practice" : s.score) : "Not complete" %></td>
		</tr>
		<%
		}
		%>
	</table>
	<h3>Reviews</h3>
	<div>
		<% Review[] reviews = currentQuiz.getReviews();
		for (int cur = 0; cur < reviews.length; cur++) {
			Review cur_review = reviews[cur];
		%>
			<div><b>At <%=sdf.format(cur_review.time)%>:</b> <%=cur_review.content %></div>
		<% }%>
	</div>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">View Quiz Details</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>