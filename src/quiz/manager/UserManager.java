package quiz.manager;

import java.sql.Connection;
import java.sql.SQLException;

import quiz.base.*;
import quiz.model.*;

public class UserManager {

	private static Connection db;
	
	static {
		db = DBConnector.getConnection();
	}
	
	public static User authenticate(String email, String password) {
		return null;
	}
	
	public static User addUser(String username, String password, String email, int is_admin) {
		return null;
	}
	
	public static boolean removeUser(User u) {
		try {
			return db.prepareStatement("DELETE FROM user WHERE user_id = " + u.user_id).execute();
		} catch (SQLException e) {
			return false;
		}
	}
	
	public static User getUser(int user_id) {
		return null;
	}
	
}
