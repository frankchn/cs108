<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication" %>

<%

QuestionResponse currentQ = (QuestionResponse)request.getAttribute("currentQuestion");

%>

<h3>Question</h3>
<p>Please enter all relevant information about the question here. If you leave the Image URL
   field blank, no image will be displayed. This combines the question text and picture
   response capabilities.</p>
<table width="700" cellspacing="3" cellpadding="3" border="0">
	<tr>
		<th>Question Text</th>
		<td>
			<textarea name="question_text" style="width:500px;height:80px;"><%=currentQ.question_text %></textarea>
		</td>
	</tr>
	<tr>
		<th>Image URL</th>
		<td><input name="question_image" type="text" value="<%=currentQ.question_image %>" style="width:500px"></td>
	</tr>
</table>
<h3>Solutions</h3>
<td>We support multiple solutions with different scores. For instance, answering "JFK" can be awarded
    0.5 points while answering "John Fitzgerald Kennedy" can be awarded 2.0 points. Awarding an option
    0.0 points will cause it to be deleted upon saves.</td>
<table id="tblOptions" width="700" cellspacing="6" cellpadding="6" border="0">
	<tr>
		<th width="500">Solution Text</th>
		<th>Score</th>
	</tr>
	<tbody>
		<%
		for(String ans : currentQ.correct_answers.keySet()) {
		%>
		<tr>
			<td><input type="text" name="correct_answer_key" style="width:100%" value="<%=ans %>"></td>
			<td><input type="text" name="correct_answer_score" style="width:100%" value="<%=currentQ.correct_answers.get(ans) %>" /></td>
		</tr>
		<%
		}
		%>
	</tbody>
	<tfoot>
		<tr>
			<td align="center" colspan="3"><a href="#" class="add_new_option">Add New Option</a></td>
		</tr>
	</tfoot>
</table>
<input type="submit" value="Update Question">
<script type="text/javascript">

$(".add_new_option").click(function (e) {
	e.preventDefault();
	$('#tblOptions > tbody:last').append(
		'<tr>' +
			'<td><input type="text" name="correct_answer_key" style="width:100%"></td>' +
			'<td><input type="text" name="correct_answer_score" value="0" style="width:100%" /></td>' +
		'</tr>'
	);
});

	</script>
