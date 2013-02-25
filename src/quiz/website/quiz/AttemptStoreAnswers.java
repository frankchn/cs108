package quiz.website.quiz;

import java.io.IOException;
import java.sql.Timestamp;

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
		QuizQuestion.QuizQuestionAttempt[] currentQQAs = currentAttempt.getQuizQuestionAttempts(false);
		QuizQuestion.QuizQuestionAttempt onlyGradedQQA = null;
		
		for(QuizQuestion.QuizQuestionAttempt QQA : currentQQAs) {
			if(QQA.graded == QuizQuestionAttemptGraded.incomplete) {
				onlyGradedQQA = QQA;
				QQA.saveAnswer(request.getParameterMap());
			}
		}
		
		if(request.getParameter("save") != null) {
			// this is a save quiz request, we just redirect and return
			response.sendRedirect("attempt.jsp?quiz_attempt_id=" + currentAttempt.quiz_attempt_id);
			return;
		}
		
		int undone_questions = 0;
		QuizQuestion.QuizQuestionAttempt chosenQQA = null;
		
		// assume from now on this is a submit answer request
		for(QuizQuestion.QuizQuestionAttempt QQA : currentQQAs) {
			if(QQA.graded == QuizQuestionAttemptGraded.incomplete) {
				QQA.gradeAnswer();
			}
			if(QQA.graded == QuizQuestionAttemptGraded.undone) {
				undone_questions++;
				if(chosenQQA == null)
					chosenQQA = QQA;
			}
		}
		
		if(!currentQuiz.multiple_pages || undone_questions == 0) {
			double score = 0.0;
			for(QuizQuestion.QuizQuestionAttempt QQA : currentQQAs) {
				score += QQA.score;
			}
			
			currentAttempt.submission_time =  new Timestamp(System.currentTimeMillis());
			currentAttempt.finished = true;
			currentAttempt.score = score;
			currentAttempt.save();
			
			response.sendRedirect("results.jsp?quiz_attempt_id=" + currentAttempt.quiz_attempt_id);
		} else {
			chosenQQA.graded = QuizQuestionAttemptGraded.incomplete;
			chosenQQA.save();
			
			if(currentQuiz.immediate_correction) {
				response.sendRedirect("results.jsp?quiz_attempt_id=" + currentAttempt.quiz_attempt_id + "&quiz_attempt_question_id=" + onlyGradedQQA.quiz_attempt_question_id);	
			} else {
				response.sendRedirect("attempt.jsp?quiz_attempt_id=" + currentAttempt.quiz_attempt_id);
			}
		}
	}

}
