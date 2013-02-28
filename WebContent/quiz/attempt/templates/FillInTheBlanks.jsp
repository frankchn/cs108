<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.*, java.util.*, java.util.regex.*" %>
<%

FillInTheBlanks.FillInTheBlanksQuestionAttempt currentQQA = 
	(FillInTheBlanks.FillInTheBlanksQuestionAttempt) request.getAttribute("QuizQuestionAttempt");
FillInTheBlanks currentQuestion = (FillInTheBlanks) currentQQA.getQuizQuestion();
QuizAttempt currentAttempt = (QuizAttempt) request.getAttribute("QuizAttempt");

String html_pattern = "(\\x5b)(\\d*)(\\x5d)";
String readonly_html = currentQQA.graded == QuizQuestionAttemptGraded.done ? "readonly" : "";

%>
<h4>Question</h4>
<%=currentQuestion.question_text.replaceAll(html_pattern, "<input " + readonly_html + " id='qqa_" + currentQQA.quiz_attempt_question_id + "_$2' type='text' name='qqa_" + currentQQA.quiz_attempt_question_id + "_$2' />") %>

<script type="text/javascript">

<% for(Integer key : currentQQA.answer.keySet()) { %>
	$('#qqa_<%=currentQQA.quiz_attempt_question_id%>_<%=key%>').val('<%=currentQQA.answer.get(key)%>');
<% } %>

</script>
<% if(currentQQA.graded == QuizQuestionAttemptGraded.done) { %>
<h4>Solutions</h4>
<% for(Integer blank_id : currentQuestion.correct_answers.keySet()) { %>
<div style="margin-top:10px">For Blank <%=blank_id %>, we accepted the following answers:
<% for(String solution : currentQuestion.correct_answers.get(blank_id).keySet()) { %>
<li><%=solution %> <%=currentAttempt.show_score ? "<em><small>(" + currentQuestion.correct_answers.get(blank_id).get(solution) + " points)</small></em>" : "" %></li>
<% } %>
</div>
<% } %>
<% if(currentAttempt.show_score) { %>
<div style="margin-top:20px;"><em>For the above answers, you scored <strong><%=currentQQA.score %></strong> points.</em></div>
<%
}
%>
<% } %>