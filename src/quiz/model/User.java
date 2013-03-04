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
	
	public static void rateQuiz(int user_id, int quiz_id, int rating) {
		try {
			PreparedStatement p = db.prepareStatement("DELETE from `rating` WHERE `user_id` = ? AND `quiz_id` = ?");
			p.setInt(1, user_id);
			p.setInt(2,  quiz_id);
			p.executeUpdate();
			PreparedStatement p2 = db.prepareStatement("INSERT into `rating` VALUES (?, ?, ?)");
			p2.setInt(1,  user_id);
			p2.setInt(2,  quiz_id);
			p2.setInt(3, rating);
			p2.executeUpdate();
		} catch (SQLException e) {
		}
	}
	
	public int getRating(int quiz_id) {
		try {
			PreparedStatement p = db.prepareStatement("SELECT * from `rating` WHERE `user_id` = ? and `quiz_id` = ?");
			p.setInt(1, user_id);
			p.setInt(2, quiz_id);
			ResultSet r = p.executeQuery();
			if (!r.next()) {
				return 0;
			} else {		   
				int curRating = r.getInt("stars");
				return curRating;
			}
		} catch (SQLException e) {
			return 0;
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
			PreparedStatement p = db.prepareStatement("SELECT ui, qi, max_start FROM (\n" +
							"SELECT user_id AS ui, quiz_id AS qi, MAX( start_time ) AS max_start\n" +
							"FROM  `quiz_attempt`\n" +
							"GROUP BY ui, qi)qa\n" +	
							"WHERE ui =?\n" +
							"ORDER BY max_start DESC");
			p.setInt(1, user_id);
			r = p.executeQuery();
			while (r.next()) {
				Record record = new Record(r.getInt("ui"), r.getInt("qi"), r.getTimestamp("max_start"));
				r_list.add(record);
			}
			return r_list;
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
			e.printStackTrace();
		}
		return requests;
	}
	
	public List<User> getFriends() {
		List<User> friends = new ArrayList<User>();
		try {
			PreparedStatement p = db.prepareStatement("SELECT * FROM `friend` WHERE `user_id_1` =" + user_id + " OR `user_id_2` =" + user_id);
			ResultSet r = p.executeQuery();
			while(r.next()) {
				int u1 = r.getInt("user_id_1");
				int u2 = r.getInt("user_id_2");
				int friend_id = u1 == user_id? u2 : u1;
				User u = User.getUser(friend_id);
				friends.add(u);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
