<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*" %>

<%
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
%>

<ex:push key="body.content">
	<% if(currentUser != null) { %>
	<div style="float:right;padding-left:15px">
		<form method="post" action="quiz/edit/CreateQuizServlet">
			<input type="submit" style="margin:15px;font-size:17px;font-weight:bold" value="Create New Quiz" />
		</form>
	</div>
	<% } %>

	<p>Here are a list of all the quizzes that are created and current available
	   to take. Clicking on any one of them will take you to a page with more
	   details and where you can take the quiz. You can also click the button on
	   the right to create a new quiz.</p>
	   
	<div id="quiz_list_container">
		<%
		Quiz[] list = QuizManager.getAllQuizzes(currentUser);
		if(list != null) {
			for(Quiz q : list) {
			%>
			<div class="quiz_item_container">
				<div class="quiz_item_right">
					<div><strong>0</strong> questions</div>
					<div><strong>0</strong> attempts</div>
					<div><strong>0.0</strong> / 5.0 rating</div>
				</div>
				<div class="quiz_item_left">
					<div class="quiz_item_title"><a href="quiz/info.php?quiz_id=<%=q.quiz_id%>"><%=q.name %></a></div>
					<div class="quiz_item_author">
						created by <a href="profile/?user_id=<%=q.user_id%>"><%=User.getUser(q.user_id).name %></a>
						on <%=sdf.format(q.created) %>
						&bull;
						<%=q.is_public ? "Public" : "Draft (Invisible)" %>
						<%=currentUser != null && currentUser.user_id == q.user_id ? "&bull; Edit This Quiz" : "" %>
					</div>
				</div>
			</div>
			<% 
			}
		} else { 
		%>
		<div style="text-align:center">
			Sorry, an error occurred. Please contact system administrators for more information.
		</div>
		<% 
		}
		%>
	</div>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Quiz Listing</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>