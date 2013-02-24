<%@tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="pageTitle" %>
<%@tag import="quiz.model.*" %>
<%
User currentUser = (User) session.getAttribute("currentUser");
%>
<!DOCTYPE html>
<html lang="en-US">
	<head>
		<base href="${pageContext.request.contextPath}/" />
		<meta charset="utf-8" />
		<link rel="stylesheet" href="static/css/main.css" />
		<title>CS108 Quiz Website | ${pageTitle}</title>
	</head>
	<body>
		<div id="container">
			<div id="topbar">
				<div id="top_nav">
					<div id="navbar_items">
						<span class="nav_item"><a href="">Home</a></span>
						<span class="nav_item"><a href="quiz/">Quizzes</a></span>
						<%
						
						if(currentUser == null) {
						%>
						<span class="nav_item"><a href="auth/login.jsp">Login</a></span>
						<span class="nav_item"><a href="auth/register.jsp">Register</a></span>
						<%
						} else {
						%>
						<span class="nav_item"><a href="messages/">Messages</a></span>
						<span class="nav_item"><a href="auth/LogoutServlet">Logout</a></span>
						<%
						}
						%>
						<%
						if(currentUser != null && currentUser.is_admin) {
						%>
						<span class="nav_item"><a href="admin/">Admin</a></span>
						<%
						}
						%>
					</div>
					<div id="greeting_msg">
					<% if(currentUser == null) { %>
						Welcome, Guest!
					<% } else { %>
						Welcome, <a href="profile/"><%=currentUser.name %></a>!
					<% } %>
					</div>
				</div>
				<div id="page_title">
					<div id="page_title_header">CS108 Quiz Website</div>
					<div id="page_title_text">${pageTitle}</div>
				</div>
			</div>
			<div id="content">
				<jsp:doBody />
			</div>
			<div id="footer">
				A CS108 Project by Angela Yeung, Jessica Liu and Frank Chen.
			</div>
		</div>
	</body>
</html>