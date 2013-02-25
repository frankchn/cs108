<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.*" %>

<%

QuestionResponse.QuestionResponseQuestionAttempt currentQQA = 
	(QuestionResponse.QuestionResponseQuestionAttempt) request.getAttribute("QuizQuestionAttempt");
QuestionResponse currentQuestion = (QuestionResponse) currentQQA.getQuizQuestion();
QuizAttempt currentAttempt = (QuizAttempt) request.getAttribute("QuizAttempt");

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
<% if(currentQQA.graded != QuizQuestionAttemptGraded.done) { %>
<h4>Answer</h4>
<input type="text" style="width:95%" name="qqa_<%=currentQQA.quiz_attempt_question_id %>" value="<%=currentQQA.answer %>">
<% } else { %>
<h4>Your Answer</h4>
<p><%=currentQQA.answer %></p>
<% if(currentAttempt.show_score) { %>
<p><em>For the above answer, you achieved a score of <strong><%=currentQQA.score %></strong>.</em>
<% } %>
<h4>Possible Answers</h4>
<p>We accepted the following answers:</p>
<ul>
<% for(String ans : currentQuestion.correct_answers.keySet()) { %>
<li><%=ans %> &mdash; <em><%=currentQuestion.correct_answers.get(ans) %> points</em></li>
<% } %>
</ul>
<% } %>