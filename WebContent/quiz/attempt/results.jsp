<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="ex" uri="http://frankchn.stanford.edu/cs108/" %>
<%@page import="quiz.model.*, quiz.manager.*, quiz.website.auth.*" %>

<%

java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();

if(!Authentication.require_login(request, response)) return;
User currentUser = (User) session.getAttribute("currentUser");
QuizAttempt currentAttempt = QuizAttempt.load(Integer.parseInt(request.getParameter("quiz_attempt_id")));
if(currentAttempt.user_id != currentUser.user_id && !currentUser.is_admin) return;
Quiz currentQuiz = Quiz.getQuiz(currentAttempt.quiz_id);

QuizQuestion.QuizQuestionAttempt[] currentQQAs = currentAttempt.getQuizQuestionAttempts(true);

%>

<ex:push key="body.content">
<h4>You started this quiz on <%=sdf.format(currentAttempt.start_time) %> and completed it on <%=sdf.format(currentAttempt.submission_time) %>.
<% if(currentAttempt.show_score) { %>
<br>You have scored a total of <em><u><%=currentAttempt.score %></u></em> points on this quiz.
<% } %>
</h4>

<%
for(QuizQuestion.QuizQuestionAttempt QQA : currentQQAs) {
	request.setAttribute("QuizAttempt", currentAttempt);
	request.setAttribute("currentQuestionType", QQA.getClass().getEnclosingClass().getSimpleName());
	request.setAttribute("QuizQuestionAttempt", QQA);
%>
<div style="padding:10px;margin-bottom:20px;background-color:#f6f6f6">
	<jsp:include page="templates/${requestScope['currentQuestionType']}.jsp" />
</div>
<%
}
%>
</ex:push>

<t:standard>
    <jsp:attribute name="pageTitle">Quiz Results</jsp:attribute>
	<jsp:body>${requestScope['body.content']}</jsp:body>
</t:standard>