<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*" %>

<%
User currentUser = (User) session.getAttribute("currentUser");
%>

<ex:push key="body.content">
	<p style="float:right;padding-left:15px">
		<a href="quiz/createquiz.jsp">Create New Quiz</a>
	</p>

	<p>Here are a list of all the quizzes that are created and current available
	   to take. Clicking on any one of them will take you to a page with more
	   details and where you can take the quiz. You can also click the button on
	   the left to create a new quiz.</p>
	   
	<div id="quiz_list_container">
		<%
		Quiz[] list = QuizManager.getAllQuizzes(currentUser);
		if(list != null) {
			for(Quiz q : list) {
			%>
			<div id="quiz_item_container">
			
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
    <jsp:attribute name="pageTitle">Quiz</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>