package quiz.messaging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import quiz.base.DBConnector;

public class MessageManager {
	private static Connection db;

	static {
		db = DBConnector.getConnection();
	}
	
	public static int numNewNotifications(int user_id) {
		return numFriendReqs(user_id) + numNewMessages(user_id);
	}
	
	public static int numFriendReqs(int user_id) {
		try {
			ResultSet friendRequests = db.prepareStatement("SELECT COUNT(*) FROM `friend_request` WHERE `requestee_user_id` = " + user_id).executeQuery();	
			int numReqs = 0;			
			if (friendRequests.next()) {
				numReqs += friendRequests.getInt(1);
			}
			return numReqs;
		} catch (SQLException e) {
			return 0;
		}
	}
	
	public static int numNewMessages(int user_id) {
		try {
			ResultSet messages = db.prepareStatement("SELECT COUNT(*) FROM `message` WHERE `recipient_user_id` = " + user_id + " and `unread`=" + 1).executeQuery();			
			int numMsgs = 0;
			if (messages.next()) {
				numMsgs += messages.getInt(1);
			}
			return numMsgs;
		} catch (SQLException e) {
			return 0;
		}
	}
	
	public static void sendMessage(Message msg) {
		//Write sql query to update to read
		
		try {
			PreparedStatement ps = db.prepareStatement("INSERT INTO `message` VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setInt(1, msg.message_id);
			ps.setString(2, msg.type);
			ps.setInt(3, msg.sender_user_id);
			ps.setInt(4, msg.recipient_user_id);
			ps.setInt(5,  msg.unread ? 1 : 0);
			ps.setTimestamp(6,  msg.time_sent);
			ps.setInt(7, msg.quiz_id);
			ps.setString(8,  msg.subject);
			ps.setString(9, msg.body);
			ps.executeUpdate();
		} catch (SQLException e) { }
	}
	
	public static void deleteMessage(int msg_id) {
		try {
			db.prepareStatement("DELETE FROM `message` WHERE `message_id` =" + msg_id).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

	public static List<Message> getMessages(int user_id) {
		ArrayList<Message> inbox = new ArrayList<Message>();
		ResultSet r;
		try {
			r = db.prepareStatement("SELECT * FROM `message` WHERE `recipient_user_id` = " + user_id).executeQuery();
			while(r.next()) {
				Message m = new Message(r.getInt("message_id"),
										r.getString("type"),
										r.getInt("sender_user_id"),
										r.getInt("recipient_user_id"),
										r.getInt("unread"),
										r.getTimestamp("time_sent"),
										r.getInt("quiz_id"),
										r.getString("subject"),
										r.getString("body"));
				inbox.add(m);
			}
			return inbox;
		} catch (SQLException e) {
			return null;
		}
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
