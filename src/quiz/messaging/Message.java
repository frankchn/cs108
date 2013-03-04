package quiz.messaging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import quiz.base.DBConnector;
import quiz.model.Achievement;
import quiz.model.QuizAttempt;

public class Message {
	public int message_id;
	public String type;
	public int sender_user_id;
	public int recipient_user_id;
	public boolean unread;
	public Timestamp time_sent;
	public int quiz_id;
	public String subject;
	public String body;

	private static Connection db = DBConnector.getConnection();
	
	public Message(int message_id, String type, int sender_user_id, int recipient_user_id, int unread, Timestamp time_sent, int quiz_id, String subject, String body) {
		this.message_id = message_id;
		this.type = type;
		this.sender_user_id = sender_user_id;
		this.recipient_user_id = recipient_user_id;
		this.unread = unread == 1 ? true : false;
		this.time_sent = time_sent;
		this.quiz_id = quiz_id;
		this.subject = subject;
		this.body = body;
	}
	
	/*public void deleteMessage() {
		try {
			PreparedStatement ps = db.prepareStatement("DELETE FROM `message` WHERE `message_id` = " +this.message_id);
			ps.executeUpdate();
		} catch (SQLException e) { }
	}*/
	
	public void markRead() {
		try {
			PreparedStatement ps = db.prepareStatement("UPDATE `message` SET `unread`= ? WHERE `message_id` = ?");
			ps.setInt(1, 0);
			ps.setInt(2, this.message_id);
			ps.executeUpdate();
		} catch (SQLException e) { }
	}
	
	public void markUnread() {
		try {
			PreparedStatement ps = db.prepareStatement("UPDATE `message` SET `unread`= ? WHERE `message_id` = ?");
			ps.setInt(1, 1);
			ps.setInt(2, this.message_id);
			ps.executeUpdate();
		} catch (SQLException e) { }
	}

	
}
