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
	<div style="margin-top:20px;">
		<form method="post" action="quiz/attempt/QuizStartAttempt">
			<input type="hidden" name="quiz_id" value="<%=currentQuiz.quiz_id %>">
			<input type="submit" style="font-size:16px;padding:5px;" name="real_mode" value="Take Quiz for Real">
			<% if(currentQuiz.practice_mode) { %>
			<input type="submit" style="font-size:16px;padding:5px;" name="practice_mode" value="Take Quiz for Practice">
			<% } %>
		</form>
	</div>
	<h3>Previous Attempts</h3>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">View Quiz Details</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>