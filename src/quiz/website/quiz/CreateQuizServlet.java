package quiz.website.quiz;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.manager.*;
import quiz.model.*;
import quiz.website.auth.Authentication;

/**
 * Servlet implementation class CreateQuizServlet
 */
@WebServlet("/quiz/edit/CreateQuizServlet")
public class CreateQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateQuizServlet() {
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
		Quiz q = QuizManager.createNewQuiz(currentUser);
		
		response.sendRedirect("quiz_info.jsp?quiz_id=" + q.quiz_id);
		
	}

}
