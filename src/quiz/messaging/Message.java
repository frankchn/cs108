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
	
	public void sendMessage(Message msg) {
		//Write sql query to update to read
		
		try {
			PreparedStatement ps = db.prepareStatement("INSERT INTO `message` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, msg.message_id);
			ps.setString(2, msg.type);
			ps.setInt(3, msg.sender_user_id);
			ps.setInt(4, msg.recipient_user_id);
			ps.setInt(5,  unread ? 1 : 0);
			ps.setTimestamp(6,  msg.time_sent);
			ps.setInt(7, msg.quiz_id);
			ps.setString(8,  msg.subject);
			ps.setString(9, msg.body);
			ps.executeUpdate();
		} catch (SQLException e) { }
	}
	
	public void deleteMessage() {
		try {
			PreparedStatement ps = db.prepareStatement("DELETE FROM `message` WHERE `message_id` = " +this.message_id);
			ps.executeUpdate();
		} catch (SQLException e) { }
	}
	
	public void markRead() {
		try {
			PreparedStatement ps = db.prepareStatement("UPDATE `message` SET `unread`= ? WHERE `message_id` = ?");
			ps.setInt(1, 0);
			ps.setInt(2, this.message_id);
			ps.executeUpdate();
		} catch (SQLException e) { }
	}
	
	
	public static Message getMessage(int message_id) {
		ResultSet r;
		try{
			r = db.prepareStatement("SELECT * FROM `message` WHERE `message_id` = " + message_id).executeQuery();
			if (r.next()) {
				Message m = new Message(r.getInt("message_id"),
						r.getString("type"),
						r.getInt("sender_user_id"),
						r.getInt("recipient_user_id"),
						r.getInt("unread"),
						r.getTimestamp("time_sent"),
						r.getInt("quiz_id"),
						r.getString("subject"),
						r.getString("body"));
				return m;
			}
			return null;
		} catch (SQLException e) {
			return null;
		}
	}
	
}
