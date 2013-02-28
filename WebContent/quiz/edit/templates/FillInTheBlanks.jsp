<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication" %>
<%

FillInTheBlanks currentQ = (FillInTheBlanks)request.getAttribute("currentQuestion");

%>
<h3>Question</h3>
<p>Note: for fill in the blanks questions, the question text should be in the format:
<em>Abraham [1] was the 16th [2] of the [3]</em>, where [1], [2] and [3] denote blanks
for students to fill in. The system accepts multiple answers with different point values.</p>
<table width="700" cellspacing="3" cellpadding="3" border="0">
	<tr>
		<th>Question Text</th>
		<td>
			<textarea name="question_text" style="width:500px;height:80px;"><%=currentQ.question_text %></textarea>
		</td>
	</tr>
</table>
<h3>Solutions</h3>
<table id="tblOptions" width="700" cellspacing="6" cellpadding="6" border="0">
	<thead>
		<tr>
			<th>Blank #</th>
			<th width="400">Solution Text</th>
			<th>Score</th>
		</tr>
	</thead>
	<tbody>
<%
for(Integer blank_id : currentQ.correct_answers.keySet()) {
	for(String ans : currentQ.correct_answers.get(blank_id).keySet()) {
%>
<tr>
	<td><input type="text" name="correct_answer_blank" style="width:100%" value="<%=blank_id %>"></td>
	<td><input type="text" name="correct_answer_key" style="width:100%" value="<%=ans %>"></td>
	<td><input type="text" name="correct_answer_score" style="width:100%" value="<%=currentQ.correct_answers.get(blank_id).get(ans) %>" /></td>
</tr>
<%
	}
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
			'<td><input type="text" name="correct_answer_blank" value="1" style="width:100%"></td>' + 
			'<td><input type="text" name="correct_answer_key" style="width:100%"></td>' +
			'<td><input type="text" name="correct_answer_score" value="1" style="width:100%" /></td>' +
		'</tr>'
	);
});

</script>