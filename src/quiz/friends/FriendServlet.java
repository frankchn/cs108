package quiz.friends;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.NameValuePair;

import com.mysql.jdbc.log.Log;

/**
 * Servlet implementation class FriendServlet
 */
@WebServlet("/FriendServlet")
public class FriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendServlet() {
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
		/*int requestor = Integer.parseInt(request.getParameter("requestor_id"));
		int requestee = Integer.parseInt(request.getParameter("requestee_id"));
		java.util.Date date = new java.util.Date();
		Timestamp time = new Timestamp(date.getTime());
		if (request.getParameter("addFriendSearch") != null) {
			// You are adding a friend!
			RelationManager.addFriendRequest(requestor, requestee, time);
			String query = request.getParameter("search_query");
			response.sendRedirect("user/search.jsp?search_query=" + query);
		} else if (request.getParameter("addFriendProf") != null) {
			// You are adding a friend!
			RelationManager.addFriendRequest(requestor, requestee, time);
			response.sendRedirect("user/profile.jsp?user=" + requestee);
		} else if (request.getParameter("confirm") != null){
			// You accepted a friend!
			RelationManager.removeFriendRequest(requestee, requestor);
			RelationManager.addFriendship(requestor, requestee, time);
			response.sendRedirect("user/profile.jsp?user=" + requestee);
		} else if (request.getParameter("confirmMsg") != null) {
			RelationManager.removeFriendRequest(requestee, requestor);
			RelationManager.addFriendship(requestor, requestee, time);
			response.sendRedirect("messaging/friendRequests.jsp");		
		} else if (request.getParameter("ignoreSearch") != null) {
			// You aren't really friends!
			RelationManager.removeFriendRequest(requestee, requestor);
			String query = request.getParameter("search_query");
			response.sendRedirect("user/search.jsp?search_query=" + query);
		} else if (request.getParameter("ignoreMsg") != null) {
			RelationManager.removeFriendRequest(requestee, requestor);
			response.sendRedirect("messaging/friendRequests.jsp");
		} else if (request.getParameter("ignoreProf") != null) {
			// You aren't really friends!
			RelationManager.removeFriendRequest(requestee, requestor);
			response.sendRedirect("user/profile.jsp?user=" + requestee);
		} else {
			RelationManager.removeFriendship(requestor, requestee);
			response.sendRedirect("user/profile.jsp?user=" + requestee);
		}*/
		
	}

}
