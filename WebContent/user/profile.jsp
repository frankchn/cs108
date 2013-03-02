<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.website.auth.Authentication, java.util.*, quiz.friends.*" %>

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
				<!--<input type="image" name="unfriend" src="user/friendbtn.png" width="55" height="16" onMouseOver="this.src='user/removebtn.png'" onMouseOut="this.src='user/friendbtn.png'"/>-->	
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
			<img style="margin-right:25px" src="<%=achievements.get(i).img %>" title="<%=currentUser.name %><%=achievements.get(i).description %>">
		<%} %>
		</div>
	<tab><h3>History</h3></tab>
		<div class="record_grid">
		<%
		for (int j = 0; j < records.size(); j++) { %>
			<% Record rec = records.get(j); %>
			<p><%= rec.quiz_name %></p>
			<% for (int k = 0; k < rec.attempts.length; k++) { %>
				<p><%= sdf.format(rec.attempts[k].start_time) %>
			<% }	
		}%>
		</div>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle"><%=currentUser.name %></jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>