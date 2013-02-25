package quiz.website.quiz;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.model.*;
import quiz.website.auth.Authentication;

/**
 * Servlet implementation class AttemptStoreAnswers
 */
@WebServlet("/quiz/attempt/AttemptStoreAnswers")
public class AttemptStoreAnswers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttemptStoreAnswers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		if(!Authentication.require_login(request, response)) return;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		QuizAttempt currentAttempt = QuizAttempt.load(Integer.parseInt(request.getParameter("quiz_attempt_id")));
		if(currentAttempt.user_id != currentUser.user_id && !currentUser.is_admin) return;
		Quiz currentQuiz = Quiz.getQuiz(currentAttempt.quiz_id);

		QuizQuestion.QuizQuestionAttempt[] currentQQAs = currentAttempt.getQuizQuestionAttempts(true);
		
	}

}
