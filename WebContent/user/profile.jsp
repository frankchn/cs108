<!-- Ratings css taken from http://css-tricks.com/star-ratings/ -->

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.website.auth.Authentication, java.util.*, quiz.friends.*, quiz.website.quiz.*" %>

<%
if(!Authentication.require_login(request, response)) return;
int profile_id = Integer.parseInt(request.getParameter("user"));
User viewedUser = (User)User.getUser(profile_id);
User currentUser = (User) session.getAttribute("currentUser");
List<Achievement> achievements = viewedUser.getAchievements();
List<Record> records = viewedUser.getRecords();
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();

%>

<ex:push key="body.content">
	<div>
		<h2 style="display:inline"><span style="font-weight:normal">
		<% if (viewedUser.user_id != currentUser.user_id) {%>
			<%=viewedUser.name%>'s Profile</span></h2>
			<div class="search_user_right">
			<form style="display:inline" action="FriendServlet" method="POST">
			<%if (RelationManager.friends(currentUser.user_id, viewedUser.user_id)) { %>
				<input type="submit" style="color:grey" name="unfriend" value="Friends" onmouseover="this.value='Remove'; this.style.color='black'" onmouseout="this.value='Friends'; this.style.color='grey'" >
			<%} else if(RelationManager.requested(currentUser.user_id, viewedUser.user_id)) { %>
				<input type="submit" name="requested" value="Request Sent" disabled>
			<%} else if(RelationManager.requested(viewedUser.user_id, currentUser.user_id)) { %>
				<input type="submit" name="confirm" value="Confirm"> &nbsp;
				<input type="submit" name="ignoreProf" value="Ignore">
			<%} else { %>
				<input type="submit" name="addFriendProf" value="+1 Add Friend">
			<%} %>
				<input type="hidden" name="requestee_id" value=<%=viewedUser.user_id%>>
				<input type="hidden" name="requestor_id" value=<%=currentUser.user_id%>>
				<input type="hidden" name="search_query" value=<%=request.getParameter("search_query")%>>
			</form>
			<%if (RelationManager.friends(currentUser.user_id, viewedUser.user_id)) { %>	
				<form style="display:inline" action="MessageServlet" method="POST">
					<input type="submit" name="message" value="Message">	
					<input type="hidden" name="messengee_id" value=<%=viewedUser.user_id%>>
					<input type="hidden" name="messenger_id" value=<%=currentUser.user_id%>>
				</form>
			<%} %>
			</div>	
			<hr>	
		<%} else { %>
			Your Profile </span></h2><hr>
		<%} %>
	</div>
	
	<style>
		tab {
			text-indent: 2.0em;
		}
	</style>
	<tab><h3>Friends</h3></tab>
	<tab><h3>Achievements</h3></tab>
		<div class="achievement_grid">
		<% 
		for (int i = 0; i < achievements.size(); i++) { %>
			<img style="margin-right:25px" src="<%=achievements.get(i).img %>" title="<%=viewedUser.name %><%=achievements.get(i).description %>">
		<%} %>
		</div>
	<tab><h3>History</h3></tab>
		<div class="record_grid">
		<%
		for (int j = 0; j < records.size(); j++) { %>
			<% Record rec = records.get(j); 
			   int curRating = currentUser.getRating(rec.quiz_id);
			%>
			<div>
			<h3><%= rec.quiz_name %></h3>
		<% if (currentUser.user_id == viewedUser.user_id) { %>
			<div class="rating" id="<%=rec.quiz_id%>">
			<% for (int curStar = 0; curStar < 5; curStar++) {%>
				<span class="star" 
				id=<%= 5 - curStar %> <% if (curStar >= (5 - curRating)) { %> 
				style="color:#FDD017">
				&#9733;
				<% } else { %>style="color:#C9C299">☆<% } %></span>
			<% } %>
			</div>

			<% } %>
			<p>Last Taken: <%= sdf.format(rec.last_start_time) %></p>
			<a href="quiz/info.jsp?quiz_id=<%=Integer.toString(rec.quiz_id) %>#prevAttempts">View Quiz History</a>
			</div>
		<%}%>
					<script>
					$('.star').click(function() {
						$.post("RateQuizServlet", {
							'rating' : $(this).attr('id'),
							'user_id' :
			<%=currentUser.user_id%>
				,
							'quiz_id' : 
			$(this).parent().attr('id')
				}, function() {window.location = "user/profile.jsp?user=<%=currentUser.user_id%>"});
				});
			</script>
		</div>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle"><%=currentUser.name %></jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>