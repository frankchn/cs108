package quiz.website.quiz;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import quiz.model.*;
import quiz.manager.*;
import quiz.website.auth.Authentication;

/**
 * Servlet implementation class EditQuestion
 */
@WebServlet("/quiz/edit/EditQuestion")
public class EditQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditQuestion() {
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
		QuizQuestion currentQuestion = QuizQuestion.loadQuestion(Integer.parseInt(request.getParameter("quiz_question_id")));
		Quiz currentQuiz = Quiz.getQuiz(currentQuestion.quiz_id);
		if(!currentUser.is_admin && currentQuiz.user_id != currentUser.user_id) return;
		
		Map<String, String[]> req = request.getParameterMap();
		currentQuestion.updateQuestion(req);
		
		response.sendRedirect("index.jsp?quiz_id=" + currentQuiz.quiz_id);
	}

}
