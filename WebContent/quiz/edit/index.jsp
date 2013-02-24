<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication" %>

<%
if(!Authentication.require_login(request, response)) return;

User currentUser = (User) session.getAttribute("currentUser");
Quiz currentQuiz = Quiz.getQuiz(Integer.parseInt(request.getParameter("quiz_id")));

if(!currentUser.is_admin && currentQuiz.user_id != currentUser.user_id) return;
%>

<ex:push key="body.content">

</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Edit Quiz</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>