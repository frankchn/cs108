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
import quiz.model.Tag;
import quiz.website.auth.Authentication;

/**
 * Servlet implementation class TagQuizServlet
 */
@WebServlet("/quiz/edit/TagQuizServlet")
public class TagQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TagQuizServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int quiz_id = Integer.parseInt(request.getParameter("quiz_id"));
		String raw_tags = request.getParameter("tag_box");
		Tag.tagQuiz(quiz_id, raw_tags);
		response.sendRedirect("index.jsp?quiz_id=" + quiz_id);
		// TODO Auto-generated method stub
	}
}
