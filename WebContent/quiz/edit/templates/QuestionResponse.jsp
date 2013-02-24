<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication" %>

<%

QuestionResponse currentQ = (QuestionResponse)request.getAttribute("currentQuestion");

%>

<form method="post" action="quiz/edit/EditQuestion">
	<h3>Question</h3>
	<table width="700" cellspacing="3" cellpadding="3" border="0">
		<tr>
			<th>Question Text</th>
			<td>
				<textarea name="question_text" style="width:600px;height:80px;"><%=currentQ.question_text %></textarea>
			</td>
		</tr>
		<tr>
			<th>Image URL</th>
			<td><input name="question_image" type="text" value="<%=currentQ.question_image %>" style="width:600px"></td>
		</tr>
	</table>
	<h3>Solutions</h3>
	<table width="700" cellspacing="3" cellpadding="3" border="0">
		<tr>
			<th>Solution Text</th>
			<th>Score</th>
		</tr>
	</table>
	<input type="submit" value="Update Question">
</form>