package quiz.messaging;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.model.User;

/**
 * Servlet implementation class MessageServlet
 */
@WebServlet("/MessageServlet")
public class MessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageServlet() {
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
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 *int message_id, String type, int sender_user_id, int recipient_user_id, int unread, Timestamp time_sent, int quiz_id, String subject, String body) {

	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("sendComp") != null) {
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			int recID = Integer.parseInt(request.getParameter("id_field"));
			java.util.Date date = new java.util.Date();
			Timestamp currTime = new Timestamp(date.getTime());
			String subject = request.getParameter("subject");
			String body = request.getParameter("body");
			Message msg = new Message(-1, "GENERAL", currentUser.user_id, recID, 1, currTime, -1, subject, body);
			MessageManager.sendMessage(msg);
		} if (request.getParameter("msg_id") != null) {
			int msg_id = Integer.parseInt(request.getParameter("msg_id"));
			System.out.println("HOLY SMOKES!: " + msg_id);
		}
		response.sendRedirect("messaging/messages.jsp");	
	}

}
