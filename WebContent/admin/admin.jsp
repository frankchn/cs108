<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication" %>

<%
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();
%>

<ex:push key="body.content">
	<style type="text/css">
		div.inline {float:left;}
		.clearBoth {clear:both;}
	</style>
	<h2><a href="index.jsp">Announcements</a>
	<% if(currentUser != null && currentUser.is_admin) { %>
	&bull; Admin
	<% } %></h2><hr><br>
		<div class ="inline" style="padding-left:20px;padding-right:20px">
			<form action="admin/NewAnnouncementServlet" method="post">
				<div><input placeholder="Enter Title Here" style="width:425px;font-size:16px;font-weight:bold;" type="text" name="subject" /></div>
				<div><textarea placeholder="Enter Announcement Text Here" style="width:425px;height:100px;" name="body"></textarea></div>
				<div><input type="submit" value="Post Announcement!" /></div>
			</form>
		</div>

		<div style="float:right;padding-left:20px;padding-right:20px">
			<!--<div style="padding:5px;margin-bottom:10px;background-color:#e2e2e2">-->
			<div style="border-top-width:1px;border-top-style:solid;border-top-color:#d8d8d8;padding:2px;margin-bottom:8px;background-color:#ececec">
			<b><font size="3px">&nbsp;Post a New Announcement</font></b>
			</div>
			<form action="admin/NewAnnouncementServlet" method="post">
				<div><input placeholder="Enter Title Here" style="width:425px;font-size:15px;margin-bottom:10px" type="text" name="subject" /></div>
				<div><textarea placeholder="Enter Announcement Text Here" style="width:425px;height:100px;" name="body"></textarea></div>
				<div><input type="submit" value="Post Announcement!" /></div>
			</form>
		</div>
		<br class="clearBoth"/>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Dashboard</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>