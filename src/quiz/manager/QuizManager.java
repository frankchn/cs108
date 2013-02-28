package quiz.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import quiz.base.DBConnector;
import quiz.model.Quiz;
import quiz.model.User;

public class QuizManager {
	
	private static Connection db = DBConnector.getConnection();;	
	
	public static Quiz createNewQuiz(User currentUser) {
		try {
			PreparedStatement p = db.prepareStatement("INSERT INTO `quiz` (`name`, `created`, `user_id`) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			p.setString(1, "New Quiz");
			p.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			p.setInt(3, currentUser.user_id);
						
			int changed = p.executeUpdate();
			if(changed == 0) return null;
			
			ResultSet s = p.getGeneratedKeys();
			s.next();
			return Quiz.getQuiz(s.getInt(1));
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static Quiz[] getAllQuizzes(User currentUser) {	
		boolean is_admin = true;
		int user_id = -1;
		if(currentUser == null || !currentUser.is_admin)
			is_admin = false;
		
		if(currentUser != null)
			user_id = currentUser.user_id;
		
		PreparedStatement p;
		ResultSet r;
		
		try {
			if(is_admin)
				p = db.prepareStatement("SELECT * FROM quiz", 
						                ResultSet.TYPE_SCROLL_INSENSITIVE, 
						                ResultSet.CONCUR_READ_ONLY);
			else
				p = db.prepareStatement("SELECT * FROM quiz WHERE is_public = 1 OR user_id = " + user_id, 
		                				ResultSet.TYPE_SCROLL_INSENSITIVE, 
		                				ResultSet.CONCUR_READ_ONLY);
			
			r = p.executeQuery();
			
			// get rows
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			int i = 0;
			Quiz[] rtn = new Quiz[size];
			while(r.next()) {
				rtn[i++] = Quiz.getQuizFromResultSet(r);
			}
			
			return rtn;
		} catch (SQLException e) {
			return null;
		}
	}
	
}
