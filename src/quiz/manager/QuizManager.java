package quiz.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import quiz.base.DBConnector;
import quiz.model.Achievement;
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
			Achievement.alertCreateQuiz(currentUser.user_id);
			return Quiz.getQuiz(s.getInt(1));
		} catch (SQLException e) {
			return null;
		}
	}

	public static int getNumQuizzes() {
		ResultSet r;
		try {
			PreparedStatement p = db.prepareStatement("SELECT COUNT(*) FROM `quiz`");
			r = p.executeQuery();
			int numQuizzes = 0;			
			if (r.next()) {
				numQuizzes += r.getInt(1);
			}
			return numQuizzes;
		} catch (SQLException e) {
			return 0;
		}
	}

	public static int getNumTaken() {
		ResultSet r;
		try {
			PreparedStatement p = db.prepareStatement("SELECT COUNT(*) FROM `quiz_attempt`");
			r = p.executeQuery();
			int numQuizzes = 0;			
			if (r.next()) {
				numQuizzes += r.getInt(1);
			}
			return numQuizzes;
		} catch (SQLException e) {
			return 0;
		}
	}

	public static int getNumTags() {
		ResultSet r;
		try {
			PreparedStatement p = db.prepareStatement("SELECT COUNT(*) FROM `tag`");
			r = p.executeQuery();
			int numTags = 0;			
			if (r.next()) {
				numTags += r.getInt(1);
			}
			return numTags;
		} catch (SQLException e) {
			return 0;
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

	public static Quiz[] getSearchedQuizzes(User currentUser, String searched) {	
		if (searched.equals("")) {
			return getAllQuizzes(currentUser);
		}
		boolean is_admin = true;
		int user_id = -1;
		if(currentUser == null || !currentUser.is_admin)
			is_admin = false;

		if(currentUser != null)
			user_id = currentUser.user_id;

		PreparedStatement p;
		ResultSet r;

		try {
			if(is_admin) {
				p = db.prepareStatement("SELECT DISTINCT (quiz_id) FROM `tag` WHERE `hashtag` LIKE ?", 
						ResultSet.TYPE_SCROLL_INSENSITIVE, 
						ResultSet.CONCUR_READ_ONLY);
				p.setString(2, "%" + searched + "%");
			} else {
				p = db.prepareStatement("SELECT DISTINCT (quiz.quiz_id) FROM (`tag` JOIN `quiz` ON (quiz.quiz_id = tag.quiz_id)) WHERE (quiz.is_public = 1 OR quiz.user_id = ?) AND tag.hashtag LIKE ?", 
						ResultSet.TYPE_SCROLL_INSENSITIVE, 
						ResultSet.CONCUR_READ_ONLY);
				p.setInt(1, user_id);
				p.setString(2, "%" + searched + "%");
			}

			r = p.executeQuery();

			// get rows
			r.last();
			int size = r.getRow();
			r.beforeFirst();

			int i = 0;
			Quiz[] rtn = new Quiz[size];
			while(r.next()) {
				rtn[i++] = Quiz.getQuiz(r.getInt(1));
			}

			return rtn;
		} catch (SQLException e) {
			return null;
		}
	}

	public static ArrayList<Quiz> searchQuizzes(String searched) {	
		if (searched.equals("")) {
			return getRecentQuizzes();
		}

		PreparedStatement p;
		ResultSet r;
		ArrayList<Quiz> quizList = new ArrayList<Quiz>();

		try {
			p = db.prepareStatement("SELECT DISTINCT (quiz_id) FROM `quiz` WHERE `name` LIKE ? OR `description` LIKE ?");
			p.setString(1, "%" + searched + "%");
			p.setString(2, "%" + searched + "%");

			r = p.executeQuery();

			while(r.next()) {
				quizList.add(Quiz.getQuiz(r.getInt(1)));
			}

			return quizList;
		} catch (SQLException e) {
			return quizList;
		}
	}

	public static ArrayList<Quiz> getRecentQuizzes() {
		try {
			ArrayList<Quiz> results = new ArrayList<Quiz>();
			PreparedStatement p = db.prepareStatement("SELECT * FROM `quiz` ORDER BY 'created' DESC LIMIT 8");

			ResultSet r = p.executeQuery();
			while(r.next()) {
				Quiz q = Quiz.getQuiz(r.getInt("quiz_id"));
				results.add(q);
			}
			return results;
		} catch (SQLException e) {
			return null;
		}
	}

	public static void delete(int quiz_id) {
		try {
			db.prepareStatement("DELETE FROM `quiz_attempt` WHERE `quiz_id` = " + quiz_id).executeUpdate();
			db.prepareStatement("DELETE FROM `quiz_attempt_question` WHERE `quiz_id` = " + quiz_id).executeUpdate();
			db.prepareStatement("DELETE FROM `quiz_question` WHERE `quiz_id` = " + quiz_id).executeUpdate();
			db.prepareStatement("DELETE FROM `quiz` WHERE `quiz_id` = " + quiz_id).executeUpdate();
			db.prepareStatement("DELETE FROM `tag` WHERE `quiz_id` = " + quiz_id).executeUpdate();
		} catch (SQLException ignored) { }
	}

	public static void deleteHistory(int quiz_id) {
		try {
			db.prepareStatement("DELETE FROM `quiz_attempt` WHERE `quiz_id` = " + quiz_id).executeUpdate();
			db.prepareStatement("DELETE FROM `quiz_attempt_question` WHERE `quiz_id` = " + quiz_id).executeUpdate();
			db.prepareStatement("DELETE FROM `reviews` WHERE `quiz_id` = " + quiz_id).executeUpdate();
		} catch (SQLException ignored) { }
	}

}
