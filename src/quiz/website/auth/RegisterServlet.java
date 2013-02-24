package quiz.website.auth;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import quiz.model.*;
import quiz.manager.*;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/auth/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String full_name = request.getParameter("full_name");

		if(email == null || password == null || full_name == null) {
			response.sendRedirect("register.jsp?missing=1");
			return;
		}
		
		email = email.trim();
		full_name = full_name.trim();
		
		if(email.length() == 0 || password.length() == 0 || full_name.length() == 0) {
			response.sendRedirect("register.jsp?missing=1");
			return;
		}

		User u = UserManager.addUser(email, password, full_name, false);
		if(u == null) {
			response.sendRedirect("register.jsp?bad_email=1");
			return;
		}
		
		// Initialize Session
		HttpSession currentSession = request.getSession();
		currentSession.setAttribute("currentUser", u);
		
		response.sendRedirect("../");
	}

}
