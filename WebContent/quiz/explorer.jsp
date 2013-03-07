<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*" %>

<%
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
%>

<ex:push key="body.content">
	<h3 style="text-align:left;float:left;"><a href="quiz/index.jsp">Explorer</a> &bull; 
	    <span style="font-weight:normal;"><a href="quiz/index.jsp">Index</a></span>	    
	</h3>
	<hr style="clear:both;"/>

	<p>Explore quizzes by clicking on tags.</p>
	   
	<div id="quiz_list_container">
		<%
		Quiz[] list = QuizManager.getAllQuizzes(currentUser);
		if(list != null) {
			for(Quiz q : list) {
			%>
			<div class="quiz_item_container">
				<div class="quiz_item_right">
					<div><strong><%=q.getQuestions().length %></strong> questions</div>
					<div><strong>XXX</strong> attempts</div>
				</div>
				<div class="quiz_item_left">
					<div class="quiz_item_title"><a href="quiz/info.jsp?quiz_id=<%=q.quiz_id%>"><%=q.name %></a></div>
					<div class="quiz_item_author">
						created by <a href="profile/?user_id=<%=q.user_id%>"><%=User.getUser(q.user_id).name %></a>
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