package quiz.website.quiz;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.model.*;

/**
 * Servlet implementation class RateQuizServlet
 */
@WebServlet("/RateQuizServlet")
public class RateQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RateQuizServlet() {
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
		if (request.getParameter("rating") != null) {
			int rating = Integer.parseInt(request.getParameter("rating"));
			int user_id = Integer.parseInt(request.getParameter("user_id"));
			int quiz_id = Integer.parseInt(request.getParameter("quiz_id"));
			// update the database with the new rating
			User.rateQuiz(user_id, quiz_id, rating);
		} else {
			String review = request.getParameter("review");
			int user_id = Integer.parseInt(request.getParameter("user_id"));
			String quiz_name = request.getParameter("quiz_id");
			int quiz_id = Integer.parseInt(quiz_name.substring(quiz_name.indexOf('_') + 1));
			User.reviewQuiz(user_id, quiz_id, review);
		}
	}

}
