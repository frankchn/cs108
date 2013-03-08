<!--  Draggable functionality edited from source: http://jsfiddle.net/5DCZw/2/ -->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication, java.util.List" %>

<%
if(!Authentication.require_login(request, response)) return;

User currentUser = (User) session.getAttribute("currentUser");
Quiz currentQuiz = Quiz.getQuiz(Integer.parseInt(request.getParameter("quiz_id")));

if(!currentUser.is_admin && currentQuiz.user_id != currentUser.user_id) return;
%>

<ex:push key="body.content">
	<h3>
		Information &bull; <a
			href="quiz/edit/quiz_info.jsp?quiz_id=<%=currentQuiz.quiz_id%>">Edit</a>
		&bull; <a
			href="quiz/edit/DeleteQuizServlet?quiz_id=<%=currentQuiz.quiz_id%>">Delete</a>
		&bull; <a
			href="quiz/edit/TogglePublicServlet?quiz_id=<%=currentQuiz.quiz_id %>">
			Make <%=currentQuiz.is_public ? "Private" : "Public" %>
		</a> &bull; Clear History
	</h3>
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
	<h3>Questions</h3>
	<table cellpadding="3" cellspacing="3" border="0" width="100%">
		<thead>
			<tr>
				<th width="50">No.</th>
				<th width="500">Question Title</th>
				<th>Type</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<%
			int i = 1;
			QuizQuestion[] qs = currentQuiz.getQuestions();
			for(QuizQuestion q : qs) {
			%>
			<tr>
				<td align="center"><%=i++ %></td>
				<td align="center"><%=q.getTitle() %></td>
				<td align="center"><%=q.getFriendlyType() %></td>
				<td align="center">
					<a href="quiz/edit/edit_question.jsp?quiz_question_id=<%=q.quiz_question_id%>">Edit</a> 
					<a href="quiz/edit/DeleteQuestionServlet?quiz_question_id=<%=q.quiz_question_id%>">Delete</a> 
					<a href="quiz/edit/MoveQuestionServlet?quiz_question_id=<%=q.quiz_question_id %>&dir=up">Up</a> 
					<a href="quiz/edit/MoveQuestionServlet?quiz_question_id=<%=q.quiz_question_id %>&dir=down">Down</a> 
				</td>
			</tr>
			<% } %>
			<form method="post" action="quiz/edit/AddQuestionServlet">
				<tr>
					<td colspan="2" align="center">Add New Question</td>
					<td align="center"><select name="type">
							<option value="QuestionResponse">Question/Picture
								Response</option>
							<option value="FillInTheBlanks">Fill-in-the-Blanks</option>
							<option value="MultipleChoice">Multiple Choice</option>
					</select></td>
					<td align="center"><input type="hidden" name="quiz_id"
						value="<%=currentQuiz.quiz_id%>"> <input type="submit"
						value="Add Question"></td>
				</tr>
			</form>
		</tbody>
	</table>
	<h3>Tags</h3>
	<center>
		<div id="tag_system">
			<form style="display: inline" action="quiz/edit/TagQuizServlet"
				method="POST">
				<input type="hidden" name="quiz_id" value="<%=currentQuiz.quiz_id%>">
				<input type="text" id="tag_box" name="tag_box"
					placeholder="Drag in tags or create your own" 
					<% String currentTags = currentQuiz.getCurTagsAsString();
					if (!currentTags.equals("")) {%>
						value="<%= currentTags %>"
					<%} %>
				 size="70"></input>
				<input
					type="submit" name="search" value="Update Tags">
			</form>
			<br/><br/>
			<div id=tag_choices>
			<% List<List<Tag> > tagsToDisplay = Tag.get2DArrayOfTags(); %>
			<% for (int tag_row = 0; tag_row < tagsToDisplay.size(); tag_row++) {
				List<Tag> cur_list = tagsToDisplay.get(tag_row);
				for (int tag_index = 0; tag_index < cur_list.size(); tag_index++) {
					Tag current_tag = cur_list.get(tag_index);%>
				<div class="draggable"><%= current_tag.hashtag %></div>
			<%  } %>
				<br>
			<% } %>

			</div>
			<script>
			$(function() {
				$(".draggable").draggable({
					revert : true,
					helper : 'clone',
					start : function(event, ui) {
						$(this).fadeTo('fast', 0.5);
					},
					stop : function(event, ui) {
						$(this).fadeTo(0, 1);
					}
				}); 

				$("#tag_box").droppable({
					hoverClass : 'active',
					drop : function(event, ui) {
						this.value = this.value + $(ui.draggable).text() + " ";
					}
				});
			});
			
			
			</script>
		</div>
	</center>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Edit Quiz</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>