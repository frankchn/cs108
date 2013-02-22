<%@ tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="pageTitle" %>
<!DOCTYPE html>
<html lang="en-US">
	<head>
		<meta charset="utf-8" />
		<link rel="stylesheet" href="static/css/main.css" />
		<title>CS108 Quiz Website | ${pageTitle}</title>
	</head>
	<body>
		<div id="container">
			<div id="topbar">
				<div id="top_nav">
					<span class="nav_item">Home</span>
					<span class="nav_item">Quizzes</span>
					<span class="nav_item">Login</span>
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