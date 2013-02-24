package quiz.model;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import quiz.base.DBConnector;

public class User {

	public int user_id;
	public String name;
	public String email;
	public boolean is_admin;
	public String cookie_key;

	private static Connection db;
	
	static {
		db = DBConnector.getConnection();
	}
	
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
