<%@ tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="pageTitle" %>

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
					<span class="nav_item"><a href="">Home</a></span>
					<span class="nav_item">Quizzes</span>
					<span class="nav_item"><a href="auth/login.jsp">Login</a></span>
					<span class="nav_item"><a href="auth/register.jsp">Register</a></span>
				</div>
				<div id="page_title">
					${pageTitle}
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