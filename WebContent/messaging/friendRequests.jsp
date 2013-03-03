<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.website.auth.Authentication, java.util.*, quiz.friends.*" %>

<%
if(!Authentication.require_login(request, response)) return;
User currentUser = (User) session.getAttribute("currentUser");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();

%>

<ex:push key="body.content">
	<style>
		tab {
			text-indent: 2.0em;
		}
		.nudge {
			line-height: 1em;
		}
	</style>
	<h3 style="text-align:left;float:left;"><span style="font-weight:normal;"><a href="messaging/messages.jsp">Messages</a></span> &bull; 
	    <a href="messaging/friendRequests.jsp"">Friend Requests</a> 
	</h3>
	<div style="float:right;padding-left:15px;padding-top:5px">
		<form method="post" action="messaging/compose.jsp">
			<input type="submit" style="margin:15px;font-size:17px;font-weight:bold" value="Compose" />
		</form>
	</div>	
	<hr style="clear:both;"/>
	<tab><h3>Friends</h3></tab>
	
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">MailBox</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>