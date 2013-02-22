package quiz.website.auth;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import quiz.manager.*;
import quiz.model.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/auth/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		if(request.getParameter("email") == null || request.getParameter("password") == null) {
			response.sendRedirect("login.jsp?bad=1");
			return;
		}
		
		String email = (String) request.getParameter("email");
		String password = (String) request.getParameter("password");
		
		User user = UserManager.authenticate(email, password);
		
		if(user == null) {
			response.sendRedirect("login.jsp?bad=1");
			return;
		}
		
		HttpSession currentSession = request.getSession();
		currentSession.setAttribute("currentUser", user);
		
		response.sendRedirect("../");
		
	}

}
