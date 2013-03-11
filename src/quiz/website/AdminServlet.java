package quiz.website;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.manager.QuizManager;
import quiz.manager.UserManager;
import quiz.model.Quiz;
import quiz.model.User;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
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
		String search_query = request.getParameter("search_query");
		if (request.getParameter("admin_promote") != null) {
			int user_id = Integer.parseInt(request.getParameter("user_id"));
			UserManager.promoteUser(user_id);
			response.sendRedirect("admin/admin.jsp?user_search=" + search_query + "#edituser");
		} else if (request.getParameter("admin_demote") != null) {
			int user_id = Integer.parseInt(request.getParameter("user_id"));
			UserManager.demoteUser(user_id);
			response.sendRedirect("admin/admin.jsp?user_search=" + search_query + "#edituser");
		} else if (request.getParameter("admin_delete") != null) {
			int user_id = Integer.parseInt(request.getParameter("user_id"));
			User toDelete = User.getUser(user_id);
			UserManager.removeUser(toDelete);
			response.sendRedirect("admin/admin.jsp?user_search=" + search_query + "#edituser");
		} else if (request.getParameter("clear_history") != null) {
			int quiz_id = Integer.parseInt(request.getParameter("quiz_id"));
			QuizManager.deleteHistory(quiz_id);
			response.sendRedirect("admin/admin.jsp?quiz_search=" + search_query + "#editquiz" );
		} else if (request.getParameter("quiz_delete")!= null) {
			int quiz_id = Integer.parseInt(request.getParameter("quiz_id"));
			Quiz toDelete = Quiz.getQuiz(quiz_id);
			toDelete.delete();
			response.sendRedirect("admin/admin.jsp?quiz_search=" + search_query + "#editquiz" );
		} else {
			response.sendRedirect("admin/admin.jsp");
		}
		
	}

}
