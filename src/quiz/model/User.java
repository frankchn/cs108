package quiz.model;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import quiz.messaging.*;

import quiz.base.DBConnector;

public class User {

	public final int user_id;
	public String name;
	public String email;
	public boolean is_admin;
	public String cookie_key;

	private static Connection db = DBConnector.getConnection();
	
	public static User getUser(int user_id) {
		ResultSet r;
		try {
			r = db.prepareStatement("SELECT * FROM `user` WHERE `user_id` = " + user_id).executeQuery();
			if(!r.next()) return null;
			User u = new User(r.getInt("user_id"), 
							  r.getString("full_name"), 
							  r.getString("email"), 
							  r.getInt("is_admin") == 1 ? true : false,
							  r.getString("cookie_key"));

			return u;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static User getUserByCookieKey(String key) {
		ResultSet r;
		try {
			PreparedStatement p = db.prepareStatement("SELECT * FROM `user` WHERE `cookie_key` = ?");
			p.setString(1, key);
			r = p.executeQuery();
			
			if(!r.next()) return null;
			User u = new User(r.getInt("user_id"), 
							  r.getString("full_name"), 
							  r.getString("email"), 
							  r.getInt("is_admin") == 1 ? true : false,
							  r.getString("cookie_key"));

			return u;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public List<Achievement> getAchievements() {
		ResultSet r;
		List<Achievement> a_list = new ArrayList<Achievement>();
		try {
			r = db.prepareStatement("SELECT * FROM `achievement` WHERE `user_id` = " + user_id).executeQuery();
			while (r.next()) {
				Achievement a = new Achievement(r.getInt("user_id"), r.getInt("achievement_id"));
				a_list.add(a);
			}
			return a_list;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public List<Record> getRecords() {
		ResultSet r;
		List<Record> r_list = new ArrayList<Record>();
		try {
			r = db.prepareStatement("SELECT * from `quiz_attempt` WHERE `user_id` = " + user_id).executeQuery();
			while (r.next()) {
				Record record = new Record(r.getInt("user_id"), r.getInt("quiz_id"), r.getTimestamp("submission_time"), r.getDouble("score"), r.getInt("show_score"), r.getInt("finished"));
				r_list.add(record);
			}
			return r_list;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public List<Message> getMessages() {
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
	
	public List<FriendRequest> getRequests() {
		List<FriendRequest> requests = new ArrayList<FriendRequest>();
		ResultSet r;
		try {
			r = db.prepareStatement("SELECT * FROM `friend_request` WHERE `requestee_user_id`=" + user_id).executeQuery();
			while(r.next()) { 
				FriendRequest req = new FriendRequest(r.getInt("friend_request_id"), r.getInt("requestor_user_id"), r.getInt("requestee_user_id"), r.getTimestamp("request_time"));
				requests.add(req);
			}	
		} catch (SQLException e) {
			return null;
		}
		return requests;
	}
	
	public List<User> getFriends() {
		List<User> friends = new ArrayList<User>();
		try {
			PreparedStatement p = db.prepareStatement("SELECT * FROM `friend` WHERE `user_id_1` =" + user_id + " OR `user_id_2` =" + user_id);
			ResultSet r = p.executeQuery();
			while(r.next()) {
				User u = User.getUser(r.getInt("user_id"));
				friends.add(u);
			}
		} catch (SQLException e) {
			return null;
		}	
		return friends;
	}
	
	private User(int user_id, String name, String email, boolean is_admin, String cookie_key) {
		this.user_id = user_id;
		this.name = name;
		this.email = email;
		this.is_admin = is_admin;
		this.cookie_key = cookie_key;
	}
	
	public String setCookieKey() {
		String key = new BigInteger(140, new SecureRandom()).toString();
		
		try {
			PreparedStatement p = db.prepareStatement("UPDATE `user` SET `cookie_key` = ? WHERE `user_id` = ?");
			p.setString(1, key);
			p.setInt(2, user_id);
			p.executeUpdate();
		} catch(SQLException ignored) { 
			return null; 
		}
		
		this.cookie_key = key;
		return key;
	}
	
}
