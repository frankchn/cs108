package quiz.website.quiz;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.manager.*;
import quiz.model.*;
import quiz.model.QuizQuestion.QuizQuestionAttempt;

/**
 * Servlet implementation class QuizStartAttempt
 */
@WebServlet("/quiz/attempt/QuizStartAttempt")
public class QuizStartAttempt extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizStartAttempt() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		Random rgen = new Random();
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Quiz currentQuiz = Quiz.getQuiz(Integer.parseInt(request.getParameter("quiz_id")));		
		
		boolean practice_mode = false;
		if(request.getParameter("practice_mode") != null)
			practice_mode = true;
		
		QuizAttempt qa = QuizAttempt.newQuizAttempt(currentQuiz, currentUser, practice_mode);
		QuizQuestionAttempt[] qqas = qa.getQuizQuestionAttempts(true);
		
		if(currentQuiz.multiple_pages) {
			int randIdx = rgen.nextInt() % qqas.length;
			qqas[randIdx].graded = QuizQuestionAttemptGraded.incomplete;
			qqas[randIdx].save();
		} else {
			for(int i = 0; i < qqas.length; i++) {
				qqas[i].graded = QuizQuestionAttemptGraded.incomplete;
				qqas[i].save();
			}
		}

		response.sendRedirect("attempt.jsp?quiz_attempt_id=" + qa.quiz_attempt_id);
	}

}
