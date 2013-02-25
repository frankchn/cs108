<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.*" %>

<%

if(!Authentication.require_login(request, response)) return;
User currentUser = (User) session.getAttribute("currentUser");
QuizAttempt currentAttempt = QuizAttempt.load(Integer.parseInt(request.getParameter("quiz_attempt_id")));
if(currentAttempt.user_id != currentUser.user_id && !currentUser.is_admin) return;
Quiz currentQuiz = Quiz.getQuiz(currentAttempt.quiz_id);

QuizQuestion.QuizQuestionAttempt[] currentQQAs = currentAttempt.getQuizQuestionAttempts(currentQuiz.random_questions);

%>

<ex:push key="body.content">
<form method="post" action="quiz/attempt/AttemptStoreAnswers">
<%
for(QuizQuestion.QuizQuestionAttempt QQA : currentQQAs) {
	if(QQA.graded == QuizQuestionAttemptGraded.incomplete) {
		request.setAttribute("QuizAttempt", currentAttempt);
		request.setAttribute("currentQuestionType", QQA.getClass().getEnclosingClass().getSimpleName());
		request.setAttribute("QuizQuestionAttempt", QQA);
%>
	<div style="padding:10px;margin-bottom:20px;background-color:#f6f6f6">
		<jsp:include page="templates/${requestScope['currentQuestionType']}.jsp" />
	</div>
<%
	}
}
%>
<div>
	<input type="hidden" name="quiz_attempt_id" value="<%=currentAttempt.quiz_attempt_id %>">
	<center>
		<input type="submit" name="submit" value="Submit Answers">
		<input type="submit" name="save" value="Save Answers">
	</center>
</div>
</form>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Quiz Attempt</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>