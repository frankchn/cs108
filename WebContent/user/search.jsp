<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.website.auth.Authentication, quiz.friends.*, java.util.*" %>

<%
if(!Authentication.require_login(request, response)) return;

User currentUser = (User) session.getAttribute("currentUser");
ArrayList<User> results = RelationManager.search(request.getParameter("search_query"));
%>

<ex:push key="body.content">
	<div>
	<h2>Search results for: <span style="font-weight:normal;">&nbsp;<%=request.getParameter("search_query")%></span></h2>
		<%if (results.size() != 0) { 
			for (int i = 0; i < results.size(); i++) { 
				User u = results.get(i);
				if (u.user_id != currentUser.user_id) {
			%>
				<div class="search_user_container">
				<div class="search_user_right">
					<div>
						<form style="display:inline" action="FriendServlet" method="POST">
							<%if (RelationManager.friends(currentUser.user_id, u.user_id)) { 
							%>
							<input type="submit" name="friends" value="Friends" disabled>	
							<%} else if(RelationManager.requested(currentUser.user_id, u.user_id)) { 
							%>
							<input type="submit" name="requested" value="Request Sent" disabled>
							<%} else if(RelationManager.requested(u.user_id, currentUser.user_id)) { 
							%>
							<input type="submit" name="confirm" value="Confirm"> &nbsp;
							<input type="submit" name="ignoreSearch" value = "Ignore">
							<%} else {
							%>
							<input type="submit" name="addFriendSearch" value="+1 Add Friend">
							<% 
							}
							%>
							<input type="hidden" name="requestee_id" value=<%=u.user_id%>>
							<input type="hidden" name="requestor_id" value=<%=currentUser.user_id%>>
							<input type="hidden" name="search_query" value=<%=request.getParameter("search_query")%>>
						</form>
					</div>
				</div>
				<div class="search_user_left">
					<div class="search_user_name"><a href="user/profile.jsp?user=<%=u.user_id%>"><%=u.name %></a></div>
					<div class="search_user_email">
						<%=u.email%> 
					</div>
				</div>
			</div>
		<%		}
			}
		} else {
		%>
	</div>
	<div class="no_results">
		No results found.
	</div>
		<%	
		}
		%>
	<br>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle"><%=currentUser.name %></jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>