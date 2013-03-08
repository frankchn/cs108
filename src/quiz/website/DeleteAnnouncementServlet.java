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
 * Servlet implementation class DeleteAnnouncementServlet
 */
@WebServlet("/admin/DeleteAnnouncementServlet")
public class DeleteAnnouncementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAnnouncementServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		if(!Authentication.require_login(request, response)) return;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(!currentUser.is_admin) return;
		
		Announcement.deleteAnnouncement(Integer.parseInt(request.getParameter("announcement_id")));
		response.sendRedirect("../index.jsp");
	}

}
