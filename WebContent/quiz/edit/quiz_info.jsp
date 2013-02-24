<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication" %>

<%
if(!Authentication.require_login(request, response)) return;

User currentUser = (User) session.getAttribute("currentUser");
Quiz currentQuiz = Quiz.getQuiz(Integer.parseInt(request.getParameter("quiz_id")));

if(!currentUser.is_admin && currentQuiz.user_id != currentUser.user_id) return;
%>

<ex:push key="body.content">
	<form method="post" action="quiz/edit/EditQuizInfo">
		<table cellspacing="3" cellpadding="3" border="0">
			<tr>
				<th>Quiz Title</th>
				<td><input name="name" type="text" size="60" value="<%=currentQuiz.name %>" /></td>
			</tr>
			<tr>
				<th>Description</th>
				<td><textarea name="description" style="width:500px;height:150px"><%=currentQuiz.description %></textarea></td>
			</tr>
			<tr>
				<th>Multiple Pages</th>
				<td><input type="checkbox" name="multiple_pages" <%=currentQuiz.multiple_pages ? "checked" : "" %> value="1"> I want the quiz to have span multiple pages (one for each question).</td>
			</tr>
			<tr>
				<th>Random Questions</th>
				<td><input type="checkbox" name="random_questions" <%=currentQuiz.random_questions ? "checked" : "" %> value="1"> I want question order to be randomized.</td>
			</tr>
			<tr>
				<th>Immediate Correction</th>
				<td><input type="checkbox" name="immediate_correction" <%=currentQuiz.immediate_correction ? "checked" : "" %> value="1"> I want individual questions graded immediately.</td>
			</tr>
			<tr>
				<th>Practice Mode</th>
				<td><input type="checkbox" name="practice_mode" <%=currentQuiz.practice_mode ? "checked" : "" %> value="1"> I want to allow this quiz to be taken without a score.</td>
			</tr>
			<tr>
				<th><input type="hidden" name="quiz_id" value="<%=currentQuiz.quiz_id %>"></th>
				<td><input type="submit" value="Update the Quiz"></td>
			</tr>
		</table>
	</form>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Edit Quiz Information</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>