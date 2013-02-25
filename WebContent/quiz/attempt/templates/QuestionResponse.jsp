<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.*" %>

<%

QuestionResponse.QuestionResponseQuestionAttempt currentQQA = 
	(QuestionResponse.QuestionResponseQuestionAttempt) request.getAttribute("QuizQuestionAttempt");
QuestionResponse currentQuestion = (QuestionResponse) currentQQA.getQuizQuestion();

%>
<h4>Question</h4>
<% if(currentQuestion.question_image.length() > 0) { %>
<div>
	<center>
		<img src="<%=currentQuestion.question_image%>" border="0" />
	</center>
</div>
<% } %>
<p><%=currentQuestion.question_text %></p>
<h4>Answer</h4>
<input type="text" style="width:95%" name="qqa_<%=currentQQA.quiz_attempt_question_id %>" value="<%=currentQQA.answer %>">