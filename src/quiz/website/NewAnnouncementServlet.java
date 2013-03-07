package quiz.website;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.model.Announcement;
import quiz.model.User;
import quiz.website.auth.Authentication;

/**
 * Servlet implementation class NewAnnouncementServlet
 */
@WebServlet("/admin/NewAnnouncementServlet")
public class NewAnnouncementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewAnnouncementServlet() {
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
		if(!currentUser.is_admin) return;
		
		Announcement.newAnnouncement(currentUser, request.getParameter("subject"), request.getParameter("body"));
		response.sendRedirect("../index.jsp");
		
	}

}
