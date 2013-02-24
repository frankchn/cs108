<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.Authentication" %>

<%
if(!Authentication.require_login(request, response)) return;

User currentUser = (User) session.getAttribute("currentUser");
QuizQuestion currentQuestion = QuizQuestion.loadQuestion(Integer.parseInt(request.getParameter("quiz_question_id")));
Quiz currentQuiz = Quiz.getQuiz(currentQuestion.quiz_id);

if(!currentUser.is_admin && currentQuiz.user_id != currentUser.user_id) return;
request.setAttribute("currentQuestionType", currentQuestion.getClass().getSimpleName());
request.setAttribute("currentQuestion", currentQuestion);

%>

<ex:push key="body.content">
	<form method="post" action="quiz/edit/EditQuestion">
		<input type="hidden" name="quiz_question_id" value="<%=currentQuestion.quiz_question_id%>">
		<jsp:include page="templates/${requestScope['currentQuestionType']}.jsp" />
	</form>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Edit Question</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>