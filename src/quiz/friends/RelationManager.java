package quiz.friends;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import quiz.base.DBConnector;
import quiz.model.User;

public class RelationManager {
	private static Connection db;

	static {
		db = DBConnector.getConnection();
	}
	
	public static ArrayList<User> search(String query) {
		try {
			ArrayList<User> results = new ArrayList<User>();
			PreparedStatement p = db.prepareStatement("SELECT * FROM `user` WHERE `email` LIKE ? OR `full_name` LIKE ?");
			p.setString(1, "%" + query + "%");
			p.setString(2, "%" + query + "%");
			
			ResultSet r = p.executeQuery();
			while(r.next()) {
				User u = User.getUser(r.getInt("user_id"));
				results.add(u);
			}
			return results;
		} catch (SQLException e) {
			return null;
		}
		
	}
	
	public static int numFriendships() {
		try {
			PreparedStatement ps = db.prepareStatement("SELECT COUNT(*) FROM `friend`");
			ResultSet p = ps.executeQuery();
			int numReqs = 0;			
			if (p.next()) {
				numReqs += p.getInt(1);
			}
			return numReqs;
		} catch (SQLException e) {
			return 0;
		}
	}
	
	public static boolean friends(int user_id1, int user_id2) {
		try {
			PreparedStatement p = db.prepareStatement("SELECT * FROM `friend` WHERE (`user_id_1` =" + user_id1 + " AND `user_id_2`=" + user_id2 +") OR (`user_id_1` =" + user_id2 + " AND `user_id_2`=" + user_id1 +")");
			ResultSet r = p.executeQuery();
			if(!r.next()) return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean requested(int requestor_id, int requestee_id) {
		try{
			PreparedStatement p = db.prepareStatement("SELECT * FROM `friend_request` WHERE `requestor_user_id` =" + requestor_id + " AND `requestee_user_id`=" + requestee_id);
			ResultSet r = p.executeQuery();
			if(!r.next()) return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static boolean addFriendRequest(int requestor_id, int requestee_id, Timestamp time) {
		try {
			PreparedStatement p = db.prepareStatement("INSERT IGNORE INTO `friend_request` (`requestor_user_id`, `requestee_user_id`, `request_time`) VALUES (?, ?, ?)");
			p.setInt(1, requestor_id);
			p.setInt(2, requestee_id);
			p.setTimestamp(3, time);
						
			int changed = p.executeUpdate();
			if(changed == 0) return false;
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static void removeFriendRequest(int requestor_id, int requestee_id) {
		try {
			db.prepareStatement("DELETE FROM `friend_request` WHERE `requestor_user_id` =" + requestor_id + " AND `requestee_user_id` =" + requestee_id).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static User addFriendship(int user_id_1, int user_id_2, Timestamp time) {
		try {
			PreparedStatement p = db.prepareStatement("INSERT IGNORE INTO `friend` (`user_id_1`, `user_id_2`, `established`) VALUES (?, ?, ?)");
			p.setInt(1, user_id_1);
			p.setInt(2, user_id_2);
			p.setTimestamp(3, time);
						
			int changed = p.executeUpdate();
			if(changed == 0) return null;
			
			ResultSet s = p.getGeneratedKeys();
			s.next();
			return User.getUser(s.getInt(1));
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static void removeFriendship(int user_id_1, int user_id_2) {
		try {
			db.prepareStatement("DELETE FROM `friend` WHERE (`user_id_1` =" + user_id_1 + " AND `user_id_2`=" + user_id_2 +") OR (`user_id_1` =" + user_id_2 + " AND `user_id_2`=" + user_id_1 +")").executeUpdate();
		} catch (SQLException e) {
		}
	}
	
}
