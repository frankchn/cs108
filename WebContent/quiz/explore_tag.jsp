<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, java.util.List" %>

<%
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
String hashtag = request.getParameter("hashtag");
%>

<ex:push key="body.content">
	<h3 style="text-align:left;float:left;"><a href="quiz/explorer.jsp">Explorer</a> &bull; 
	    <span style="font-weight:normal;"><a href="quiz/index.jsp">Listing</a></span>	    
	</h3>
			<% if(currentUser != null) { %>
	<div style="float:right;padding-left:15px;position:relative;left:16px;">
		<form method="post" action="quiz/edit/CreateQuizServlet">
			<input type="submit" style="margin:15px;font-size:17px;font-weight:bold" value="Create New Quiz" />
		</form>
	</div>
	<% } %>
	<hr style="clear:both;"/>

	<p>Results for hashtag <b><%= "#" + hashtag%></b></p>

	<div id="quiz_list_container">
		<%
		Quiz[] list = QuizManager.getSearchedQuizzes(currentUser, "#" + hashtag);
		//Quiz[] list = QuizManager.getAllQuizzes(currentUser);
		if(list != null) {
			for(Quiz q : list) {
			%>
			<div class="quiz_item_container">
				<div class="quiz_item_right">
					<div><strong><%=q.getQuestions().length %></strong> questions</div>
					<div><strong><%=q.getNumAttempts() %></strong> attempts</div>
				</div>
				<div class="quiz_item_left">
					<div class="quiz_item_title"><a href="quiz/info.jsp?quiz_id=<%=q.quiz_id%>"><%=q.name %></a></div>
					<div class="quiz_item_author">
						created by <a href="user/profile.jsp?user=<%=q.user_id%>"><%=User.getUser(q.user_id).name %></a>
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