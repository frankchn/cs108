<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, java.util.List" %>

<%
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
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

	<p>Explore quizzes by clicking on tags.</p>
			<center><div id=tag_cloud>
			<% List<List<Tag> > tagsToDisplay = Tag.get2DArrayOfTags(); %>
			<% for (int tag_row = 0; tag_row < tagsToDisplay.size(); tag_row++) {
				List<Tag> cur_list = tagsToDisplay.get(tag_row);
				for (int tag_index = 0; tag_index < cur_list.size(); tag_index++) {
					Tag current_tag = cur_list.get(tag_index);%>
				<div class="draggable_empty" ><a href="quiz/explore_tag.jsp?hashtag=<%= current_tag.hashtag.substring(1) %>" style="font-size:<%= current_tag.fontSize %>px;"><%= current_tag.hashtag %></a></div>
			<%  } %>
				<br>
			<% } %>

			</div></center>
			<script>
			$(function() {
				$(".draggable_empty").draggable({
					revert : true,
					helper : 'clone',
					start : function(event, ui) {
						$(this).fadeTo('fast', 0.5);
					},
					stop : function(event, ui) {
						$(this).fadeTo(0, 1);
					}
				}); 
			});
			</script>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Quiz Listing</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>