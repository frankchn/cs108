package quiz.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import quiz.base.DBConnector;
import quiz.model.Quiz;
import quiz.model.User;

public class QuizManager {
	
	private static Connection db;	
	static {
		db = DBConnector.getConnection();
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
				rtn[i++] = new Quiz(r.getInt("quiz_id"),
									r.getString("name"),
									r.getString("description"),
									r.getInt("user_id"),
									r.getDate("created"),
									r.getInt("is_public") == 1 ? true : false,
									r.getInt("random_questions") == 1 ? true : false,
									r.getInt("multiple_pages") == 1 ? true : false,
									r.getInt("immediate_correction") == 1 ? true : false,
									r.getInt("practice_mode") == 1 ? true : false);
			}
			
			return rtn;
		} catch (SQLException e) {
			return null;
		}
	}
	
}
