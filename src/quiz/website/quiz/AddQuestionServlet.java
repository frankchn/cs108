package quiz.website.quiz;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.website.auth.Authentication;
import quiz.model.*;

/**
 * Servlet implementation class AddQuestionServlet
 */
@WebServlet("/quiz/edit/AddQuestionServlet")
public class AddQuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddQuestionServlet() {
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
		Quiz currentQuiz = Quiz.getQuiz(Integer.parseInt(request.getParameter("quiz_id")));
		if(!currentUser.is_admin && currentQuiz.user_id != currentUser.user_id) return;
		
		String type = request.getParameter("type");
		if(type == null) return;
		
		try {
			QuizQuestion.newQuestion(currentQuiz.quiz_id, Class.forName("quiz.model." + request.getParameter("type")));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Trying to creating a class that does not exist.");
		}
		
		response.sendRedirect("index.jsp?quiz_id=" + currentQuiz.quiz_id);
	}

}
