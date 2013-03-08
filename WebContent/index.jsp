<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication" %>

<%
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
%>

<ex:push key="body.content">
	<h2>Announcements
	<% if(currentUser != null && currentUser.is_admin) { %>
	&bull; <a onClick="showNewAnnouncementForm()" href="#">New</a>
	<% } %></h2>
	<div class="newannouncement" style="display:none;padding-left:20px;padding-right:20px">
		<form action="admin/NewAnnouncementServlet" method="post">
			<div><input placeholder="Enter Title Here" style="width:600px;font-size:16px;font-weight:bold;" type="text" name="subject" /></div>
			<div><textarea placeholder="Enter Announcement Text Here" style="width:600px;height:100px;" name="body"></textarea></div>
			<div><input type="submit" value="Post New Announcement!" /></div>
		</form>
	</div>
	<% for(Announcement a : Announcement.getAnnouncements()) { %>
	<div style="padding-left:20px;padding-right:20px;padding-bottom:5px;border-bottom:1px solid #ccc">
		<h4><%=a.subject %></h4>
		<div><%=a.body %></div>
		<div style="text-align:right">
			<em>
				Posted by <a href="user/profile.jsp?user=<%=a.user_id%>"><%=User.getUser(a.user_id).name %></a> on <%=sdf.format(a.posted) %>
				<% if(currentUser != null && currentUser.is_admin) { %>
				&bull; <a href="admin/DeleteAnnouncementServlet?announcement_id=<%=a.announcement_id %>">Delete</a>
				<% } %>
			</em>
		</div>
	</div>
	<% } %>
	<h2>My Recent Activity</h2>
	<h2>Friends' Activities</h2>
	<h2>Popular Quizzes</h2>
	<h2>Recent Quizzes</h2>
	<h2>Achievements Earned</h2>
	<script type="text/javascript">
	
	function showNewAnnouncementForm() {
		$(".newannouncement").show();
	}
	
	</script>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Welcome!</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>