<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication" %>

<%

MultipleChoice currentQ = (MultipleChoice)request.getAttribute("currentQuestion");

%>

<h3>Question</h3>
<p>Please enter all relevant information about the question here.</p>
<table width="800" cellspacing="3" cellpadding="3" border="0">
	<tr>
		<th width="150">Question Text</th>
		<td width="650">
			<textarea name="question_text" style="width:500px;height:80px;"><%=currentQ.question_text %></textarea>
		</td>
	</tr>
	<tr>
		<th>Select Multiple</th>
		<td>
			<input value="1" type="checkbox" name="multi_select" <%=currentQ.multi_select ? "checked" : "" %>>
			Yes, I want checkboxes (multiple selections) rather than radio buttons.
		</td>
	</tr>
</table>
<h3>Correct Options</h3>
<p>We support Gradiance-style quizzes. You can enter multiple incorrect options
   and specify how many options to appear. The selection will be randomized every time the user
   takes the quiz.</p>
<p>Correct options to appear: <input type="text" name="correct_visible" value="<%=currentQ.correct_visible%>"></p>
<table id="tblOptions_correct" width="700" cellspacing="6" cellpadding="6" border="0">
	<tr>
		<th width="500">Correct Options</th>
		<th>Select Score</th>
	</tr>
	<tbody>
		<%
		for(String ans : currentQ.correct_options.keySet()) {
		%>
		<tr>
			<td><input type="text" name="correct_answer_key" style="width:100%" value="<%=ans %>"></td>
			<td><input type="text" name="correct_answer_score" style="width:100%" value="<%=currentQ.correct_options.get(ans) %>" /></td>
		</tr>
		<%
		}
		%>
	</tbody>
	<tfoot>
		<tr>
			<td align="center" colspan="3"><a href="#" class="add_new_correct_option">Add New Option</a></td>
		</tr>
	</tfoot>
</table>
<h3>Incorrect Options</h3>
<p>We support Gradiance-style quizzes. You can enter multiple incorrect options
   and specify how many options to appear. The selection will be randomized every time the user
   takes the quiz.</p>
<p>Incorrect options to appear: <input type="text" name="incorrect_visible" value="<%=currentQ.incorrect_visible%>"></p>
<table id="tblOptions_incorrect" width="700" cellspacing="6" cellpadding="6" border="0">
	<tr>
		<th width="500">Incorrect Options</th>
		<th>Select Score</th>
	</tr>
	<tbody>
		<%
		for(String ans : currentQ.incorrect_options.keySet()) {
		%>
		<tr>
			<td><input type="text" name="incorrect_answer_key" style="width:100%" value="<%=ans %>"></td>
			<td><input type="text" name="incorrect_answer_score" style="width:100%" value="<%=currentQ.incorrect_options.get(ans) %>" /></td>
		</tr>
		<%
		}
		%>
	</tbody>
	<tfoot>
		<tr>
			<td align="center" colspan="3"><a href="#" class="add_new_incorrect_option">Add New Option</a></td>
		</tr>
	</tfoot>
</table>
<input type="submit" value="Update Question">
<script type="text/javascript">

$(".add_new_correct_option").click(function (e) {
	e.preventDefault();
	$('#tblOptions_correct > tbody:last').append(
		'<tr>' +
			'<td><input type="text" name="correct_answer_key" style="width:100%"></td>' +
			'<td><input type="text" name="correct_answer_score" value="1" style="width:100%" /></td>' +
		'</tr>'
	);
});

$(".add_new_incorrect_option").click(function (e) {
	e.preventDefault();
	$('#tblOptions_incorrect > tbody:last').append(
		'<tr>' +
			'<td><input type="text" name="incorrect_answer_key" style="width:100%"></td>' +
			'<td><input type="text" name="incorrect_answer_score" value="0" style="width:100%" /></td>' +
		'</tr>'
	);
});

	</script>
