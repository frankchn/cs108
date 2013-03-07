<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.*" %>

<%
if(!Authentication.require_login(request, response)) return;

User currentUser = (User) session.getAttribute("currentUser");
Quiz currentQuiz = Quiz.getQuiz(Integer.parseInt(request.getParameter("quiz_id")));

java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
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
			<td><a href="profile/?user_id=<%=currentQuiz.user_id%>"><%=User.getUser(currentQuiz.user_id).name %></a></td>
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
		<form method="post" action="messaging/compose.jsp" style="display:inline">
			<input type="hidden" name="quiz_id" value="<%=currentQuiz.quiz_id %>">
			<input type="hidden" name="high_score" value="<%=currentQuiz.getHighestScore(currentUser.user_id) %>"/>
			<input type="submit" style="font-size:16px;padding:5px" name="challenge" value="Challenge a Friend!">
		</form>
	</div>
	<h3 id= "prevAttempts">Previous Attempts</h3>
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
	<h3>Top Scorers</h3>
	<h3>Recent Activity</h3>
	<h3>Reviews</h3>
	<tab><div>
		<% Review[] reviews = currentQuiz.getReviews();
		for (int cur = 0; cur < reviews.length; cur++) {
			Review cur_review = reviews[cur];
		%>
			<p><b>At <%=sdf.format(cur_review.time)%>:</b> <%=cur_review.content %></p>
		<% }%>
	</div></tab>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">View Quiz Details</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>