package quiz.website.quiz;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.model.Quiz;
import quiz.model.User;
import quiz.website.auth.Authentication;

/**
 * Servlet implementation class EditQuizInfo
 */
@WebServlet("/quiz/edit/EditQuizInfo")
public class EditQuizInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditQuizInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!Authentication.require_login(request, response)) return;

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Quiz currentQuiz = Quiz.getQuiz(Integer.parseInt(request.getParameter("quiz_id")));

		if(!currentUser.is_admin && currentQuiz.user_id != currentUser.user_id) return;

		currentQuiz.name = request.getParameter("name");
		currentQuiz.description = request.getParameter("description");
		currentQuiz.multiple_pages = request.getParameter("multiple_pages") != null ? true : false;
		currentQuiz.random_questions = request.getParameter("random_questions") != null ? true : false;
		currentQuiz.immediate_correction = request.getParameter("immediate_correction") != null ? true : false;
		currentQuiz.practice_mode = request.getParameter("practice_mode") != null ? true : false;
		
		currentQuiz.save();
	}

}
