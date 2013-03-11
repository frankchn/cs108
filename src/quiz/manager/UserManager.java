package quiz.manager;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import quiz.base.*;
import quiz.model.*;

public class UserManager {

	private static Connection db = DBConnector.getConnection();

	private static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/* Adapted from http://stackoverflow.com/a/1515746 */
	private static String hash_password(String text) {
		byte[] sha1hash = new byte[40];
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(text.getBytes("utf-8"), 0, text.length());
			sha1hash = md.digest();
		} catch (Exception ignored) { }
		return hexToString(sha1hash);
	}
	
	public static User authenticate(String email, String password) {
		password = hash_password(email + password);
		
		try {			
			PreparedStatement p = db.prepareStatement("SELECT * FROM `user` WHERE `email` = ?");
			p.setString(1, email);
			
			ResultSet r = p.executeQuery();
			if(!r.next()) return null;
			if(!r.getString("password").equals(password)) return null;
			
			return User.getUser(r.getInt("user_id"));
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static User addUser(String email, String password, String full_name, boolean is_admin) {
		password = hash_password(email + password);
		
		try {
			PreparedStatement p = db.prepareStatement("INSERT IGNORE INTO `user` (`email`, `password`, `full_name`, `is_admin`) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			p.setString(1, email);
			p.setString(2, password);
			p.setString(3, full_name);
			p.setInt(4, is_admin ? 1 : 0);
						
			int changed = p.executeUpdate();
			if(changed == 0) return null;
			
			ResultSet s = p.getGeneratedKeys();
			s.next();
			return User.getUser(s.getInt(1));
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static int numAllUsers() {
		try {
			ResultSet chals = db.prepareStatement("SELECT COUNT(*) FROM `user`").executeQuery();	
			int numUsers = 0;			
			if (chals.next()) {
				numUsers += chals.getInt(1);
			}
			return numUsers;
		} catch (SQLException e) {
			return 0;
		}
	}
	
	public static void removeUser(User u) {
		try {
			db.prepareStatement("DELETE FROM user WHERE user_id = " + u.user_id).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void promoteUser(User u) {
		try {
			db.prepareStatement("").executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
