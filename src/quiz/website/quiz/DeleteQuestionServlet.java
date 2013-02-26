package quiz.website.quiz;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.model.Quiz;
import quiz.model.QuizQuestion;
import quiz.model.User;
import quiz.website.auth.Authentication;

/**
 * Servlet implementation class DeleteQuestionServlet
 */
@WebServlet("/quiz/edit/DeleteQuestionServlet")
public class DeleteQuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteQuestionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!Authentication.require_login(request, response)) return;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		QuizQuestion currentQuestion = QuizQuestion.loadQuestion(Integer.parseInt(request.getParameter("quiz_question_id")));
		Quiz currentQuiz = Quiz.getQuiz(currentQuestion.quiz_id);
		if(!currentUser.is_admin && currentQuiz.user_id != currentUser.user_id) return;
		
		currentQuestion.delete();
		
		response.sendRedirect("index.jsp?quiz_id=" + currentQuiz.quiz_id);
	}

}
