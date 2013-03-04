<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.website.auth.Authentication, java.util.*, quiz.friends.*, quiz.messaging.*" %>

<%
if(!Authentication.require_login(request, response)) return;
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
ArrayList<FriendRequest> results = MessageManager.getFriendRequests(currentUser.user_id);
int numReqs = MessageManager.numFriendReqs(currentUser.user_id);
int numMsgs = MessageManager.numNewMessages(currentUser.user_id);
%>

<ex:push key="body.content">
	<h3 style="text-align:left;float:left;"><span style="font-weight:normal;"><a href="messaging/messages.jsp">Messages<%if (numMsgs >0) { %>(<%=numMsgs%>)<%}%></a></span> &bull; 
	    <a href="messaging/friendRequests.jsp"">Friend Requests<%if (numReqs >0) { %>(<%=numReqs%>)<%}%></a> 
	</h3>
	<div style="float:right;padding-left:15px;padding-top:5px">
		<form method="post" action="messaging/compose.jsp">
			<input type="submit" style="margin:15px;font-size:17px;font-weight:bold" value="Compose" />
		</form>
	</div>	
	<hr style="clear:both;"/>
	<div style="margin-top:15px">
		<%if (results.size() != 0) { 
			for (int i = 0; i < results.size(); i++) { 
				FriendRequest f = results.get(i);
				User u = User.getUser(f.requestor_user_id);
			%>
			<div class="search_user_container">
				<div class="search_user_right" style="margin-top:1em">
					<form style="display:inline" action="FriendServlet" method="POST">
						<input type="submit" name="confirmMsg" value="Confirm"> &nbsp;
						<input type="submit" name="ignoreMsg" value = "Ignore">

						<input type="hidden" name="requestee_id" value=<%=u.user_id%>>
						<input type="hidden" name="requestor_id" value=<%=currentUser.user_id%>>
					</form>
				</div>
				<div class="search_user_left">
					<div class="search_user_name"><a href="user/profile.jsp?user=<%=u.user_id%>"><%=u.name %></a></div>
					<div class="search_user_email">
						<%=u.email%> 
					</div>
				</div>
			</div>
		<%		
			}
		} else {
		%>
		<center><h4> No new friend requests! </h4></center>
		<%	
		}
		%>
	</div>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">MailBox</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>