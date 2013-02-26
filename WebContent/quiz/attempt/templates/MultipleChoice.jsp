<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.*, java.util.*" %>
<%

MultipleChoice.MultipleChoiceQuestionAttempt currentQQA = 
	(MultipleChoice.MultipleChoiceQuestionAttempt) request.getAttribute("QuizQuestionAttempt");
MultipleChoice currentQuestion = (MultipleChoice) currentQQA.getQuizQuestion();
QuizAttempt currentAttempt = (QuizAttempt) request.getAttribute("QuizAttempt");

List<String> options = new ArrayList<String>();

for(String opt : currentQQA.correct_options.keySet()) {
	options.add(opt);
}

for(String opt : currentQQA.incorrect_options.keySet()) {
	options.add(opt);
}

Collections.shuffle(options);

%>
<h4>Question</h4>
<p><%=currentQuestion.question_text %></p>
<% if(currentQQA.graded != QuizQuestionAttemptGraded.done) { %>
<h4>Answer</h4>
<div>
	<% for(String option : options) { %>
	<p>
		<% 
		String selected_opt = "";
		if(currentQQA.answer.contains(option)) 
			selected_opt = "checked";
		
		if(currentQuestion.multi_select) { 
		%>
		<input type="checkbox" <%=selected_opt %> name="qqa_<%=currentQQA.quiz_attempt_question_id %>" value="<%=option %>">
		<% } else { %>
		<input type="radio" <%=selected_opt %> name="qqa_<%=currentQQA.quiz_attempt_question_id %>" value="<%=option %>">
		<% } %>
		<%=option %>
	</p>
	<% } %>
</div>
<% } else { 
	Map<String, Double> scoregrades = new HashMap<String, Double>();
	
	scoregrades.putAll(currentQQA.correct_options);
	scoregrades.putAll(currentQQA.incorrect_options);
%>
<h4>Your Answer</h4>
<ul>
	<% for(String option : options) { %>
	<li>
	<% if(currentQQA.answer.contains(option)) { %>
		<strong><%=option %></strong>
	<% } else { %>
		<%=option %>
	<% } %>
	<% if(currentAttempt.show_score) { %>
	<small><em>(<%=scoregrades.get(option) %> points)</em></small>
	<% } %>
	</li>
	<% } %>
</ul>
<% if(currentAttempt.show_score) { %>
<em>For the above answers, you scored <strong><%=currentQQA.score %></strong> points.</em>
<% } %>
<% } %>
