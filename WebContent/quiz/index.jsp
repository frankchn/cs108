<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*" %>

<%
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
String searchedTag = "";
if (response.getHeader("search_quiz_term") != null) {
	searchedTag = response.getHeader("search_quiz_term");
}
%>

<ex:push key="body.content">
	<h3 style="text-align:left;float:left;"><span style="font-weight:normal;"><a href="quiz/explorer.jsp">Explorer</a></span> &bull; 
	    <a href="quiz/index.jsp">Listing</a>    
	</h3>
		<% if(currentUser != null) { %>
	<div style="float:right;padding-left:15px;position:relative;left:16px;">
		<form method="post" action="quiz/edit/CreateQuizServlet">
			<input type="submit" style="margin:15px;font-size:17px;font-weight:bold" value="Create New Quiz" />
		</form>
	</div>
	<% } %>
	<hr style="clear:both;"/>


	<p>Here is a list of all the quizzes that are created and currently
		available to take. Clicking on any one of them will take you to a page
		with more details and where you can take the quiz.</p>
	
	<div style="margin-bottom:15px; position:relative; left:-1px;">
	<form style="display: inline;" action="quiz/SearchQuizServlet" method="post">
		<input type="text" name="search_by_tag"
			placeholder="Search by quiz tag" size="40"> 
		<input
			type="submit" name="search" value="Search">
	</form></div>

	<div id="quiz_list_container">
		<%
		Quiz[] list = QuizManager.getSearchedQuizzes(currentUser, searchedTag);
		if(list != null) {
			for(Quiz q : list) {
				User u = User.getUser(q.user_id);
			%>
			<div class="quiz_item_container">
				<div class="quiz_item_right">
					<div><strong><%=q.getQuestions().length %></strong> questions</div>
					<div><strong><%=q.getNumAttempts() %></strong> attempts</div>
				</div>
				<div class="quiz_item_left">
					<div class="quiz_item_title"><a href="quiz/info.jsp?quiz_id=<%=q.quiz_id%>"><%=q.name %></a></div>
					<div class="quiz_item_author">
						created by <a href="user/profile.jsp?user=<%=q.user_id%>"><%=u.name %></a>
						on <%=sdf.format(q.created) %>
						&bull;
						<%=q.is_public ? "Public" : "Draft (Invisible)" %>
						<%=(currentUser != null && (currentUser.is_admin || currentUser.user_id == q.user_id)) ? 
								"&bull; <a href='quiz/edit/index.jsp?quiz_id=" + q.quiz_id + "'>Edit This Quiz</a>"
								: "" %>
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