package quiz.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import quiz.base.DBConnector;

public class QuizAttempt {

	public final int quiz_id;
	public final int quiz_attempt_id;
	public final int user_id;
	public Timestamp start_time;
	public Timestamp submission_time;
	public double score;
	public boolean show_score;
	public boolean finished;
	
	private static Connection db = DBConnector.getConnection();

	private QuizAttempt(int quiz_attempt_id, int quiz_id, int user_id) {
		this.quiz_id = quiz_id;	
		this.quiz_attempt_id = quiz_attempt_id;
		this.user_id = user_id;
	}
	
	public void save() {
		try {
			PreparedStatement p = db.prepareStatement("UPDATE `quiz_attempt` SET `submission_time` = ?, score = ?, finished = ? WHERE `quiz_attempt_id` = ?");
			p.setTimestamp(1, submission_time);
			p.setDouble(2, score);
			p.setInt(3, finished ? 1 : 0);
			p.setInt(4, quiz_attempt_id);
			p.execute();
		} catch (SQLException e) { }
	}
	
	public static QuizAttempt[] loadTopScores(int quiz_id) {
		try {
			ResultSet r = db.prepareStatement("SELECT quiz_attempt_id FROM  `quiz_attempt` WHERE `quiz_id` = " + quiz_id + " AND `score` > 0 ORDER BY `score` DESC LIMIT 5",
					  ResultSet.TYPE_SCROLL_INSENSITIVE, 
					  ResultSet.CONCUR_READ_ONLY).executeQuery();
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			int i = 0;
			QuizAttempt[] rtn = new QuizAttempt[size];
			while(r.next()) {
				rtn[i++] = QuizAttempt.load(r.getInt("quiz_attempt_id"));
			}
			
			return rtn;
			
		} catch (SQLException e) {
			return null;
		}	
	}
	
	public static QuizAttempt[] loadRecentScores(int quiz_id) {
		try {
			ResultSet r = db.prepareStatement("SELECT quiz_attempt_id FROM  `quiz_attempt` WHERE `quiz_id` = " + quiz_id + " AND `score` IS NOT NULL ORDER BY `submission_time` DESC LIMIT 5",
					  ResultSet.TYPE_SCROLL_INSENSITIVE, 
					  ResultSet.CONCUR_READ_ONLY).executeQuery();
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			int i = 0;
			QuizAttempt[] rtn = new QuizAttempt[size];
			while(r.next()) {
				rtn[i++] = QuizAttempt.load(r.getInt("quiz_attempt_id"));
			}
			
			return rtn;
			
		} catch (SQLException e) {
			return null;
		}	
	}
	
	public QuizQuestion.QuizQuestionAttempt[] getQuizQuestionAttempts(boolean random_order) {
		try {
			String orderSQL = " ORDER BY `sort_order` ASC";
			if(random_order) orderSQL = " ORDER BY RAND()";
			
			ResultSet r = db.prepareStatement("SELECT DISTINCT `quiz_attempt_question_id` FROM `quiz_attempt_question` JOIN `quiz_question` ON `quiz_attempt_question`.`quiz_question_id` = `quiz_question`.`quiz_question_id` WHERE `quiz_attempt_id` = " + quiz_attempt_id + orderSQL, 
							    			  ResultSet.TYPE_SCROLL_INSENSITIVE, 
							    			  ResultSet.CONCUR_READ_ONLY).executeQuery();
			
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			int i = 0;
			QuizQuestion.QuizQuestionAttempt[] rtn = new QuizQuestion.QuizQuestionAttempt[size];
			while(r.next()) {
				rtn[i++] = QuizQuestion.loadQuizQuestionAttempt(r.getInt("quiz_attempt_question_id"));
			}
			
			return rtn;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static QuizAttempt newQuizAttempt(Quiz quiz, User user, boolean show_score) {
		QuizAttempt qa = null;
		
		try {
			PreparedStatement ps = db.prepareStatement("INSERT INTO `quiz_attempt` (`quiz_id`, `user_id`, `start_time`, `show_score`) VALUES (?, ?, ?, ?)",
													   Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, quiz.quiz_id);
			ps.setInt(2, user.user_id);
			ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			ps.setInt(4, show_score ? 1 : 0);
			ps.executeUpdate();
			
			if (!show_score) {
				Achievement.alertPractice(user.user_id);
			}
			
			ResultSet s = ps.getGeneratedKeys();
			s.next();
			qa = QuizAttempt.load(s.getInt(1));
		} catch (SQLException e) { }
		
		QuizQuestion[] qs = quiz.getQuestions();
		for(QuizQuestion q : qs) {
			q.newQuizQuestionAttempt(qa, user);
		}
		return qa;
	}
	
	public static QuizAttempt[] loadAttemptsForRecord(int quiz_id, int user_id) {
		try {
			ResultSet r = db.prepareStatement("SELECT `quiz_attempt_id` FROM `quiz_attempt` WHERE `quiz_id` = " + quiz_id + " AND `user_id` = " + user_id + " ORDER BY `start_time` DESC",
					  ResultSet.TYPE_SCROLL_INSENSITIVE, 
					  ResultSet.CONCUR_READ_ONLY).executeQuery();
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			int i = 0;
			QuizAttempt[] rtn = new QuizAttempt[size];
			while(r.next()) {
				rtn[i++] = load(r.getInt("quiz_attempt_id"));
			}
			
			return rtn;
			
		} catch (SQLException e) {
			return null;
		}
	}

	public static QuizAttempt[] loadAttempts(Quiz quiz, User user) {
		try {
			ResultSet r = db.prepareStatement("SELECT `quiz_attempt_id` FROM `quiz_attempt` WHERE `quiz_id` = " + quiz.quiz_id + " AND `user_id` = " + user.user_id + " ORDER BY `start_time` DESC",
											  ResultSet.TYPE_SCROLL_INSENSITIVE, 
											  ResultSet.CONCUR_READ_ONLY).executeQuery();
			
			
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			int i = 0;
			QuizAttempt[] rtn = new QuizAttempt[size];
			while(r.next()) {
				rtn[i++] = load(r.getInt("quiz_attempt_id"));
			}
			
			return rtn;
			
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static QuizAttempt load(int quiz_attempt_id) {
		try {
			ResultSet r = db.prepareStatement("SELECT * FROM `quiz_attempt` WHERE `quiz_attempt_id` = " + quiz_attempt_id).executeQuery();
			if(!r.next()) return null;
			
			QuizAttempt q = new QuizAttempt(r.getInt("quiz_attempt_id"), r.getInt("quiz_id"), r.getInt("user_id"));
			q.start_time = r.getTimestamp("start_time");
			q.submission_time = r.getTimestamp("submission_time");
			q.score = r.getDouble("score");
			q.show_score = r.getInt("show_score") == 1 ? true : false;
			q.finished = r.getInt("finished") == 1 ? true : false;
			
			return q;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static QuizAttempt[] last_day_attempt(int user_id) {
		java.util.Date date = new java.util.Date();
		Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.DATE, -1);
	    date.setTime( c.getTime().getTime() );
		Timestamp last_day = new Timestamp(date.getTime());
		try {
			ResultSet r = db.prepareStatement("SELECT `quiz_attempt_id` FROM `quiz_attempt` WHERE `user_id` = " + user_id + " AND `submission_time` > '" + last_day + "' ORDER BY `submission_time` DESC",
					  ResultSet.TYPE_SCROLL_INSENSITIVE, 
					  ResultSet.CONCUR_READ_ONLY).executeQuery();
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			int i = 0;
			QuizAttempt[] rtn = new QuizAttempt[size];
			while(r.next()) {
				rtn[i++] = load(r.getInt("quiz_attempt_id"));
			}
			
			return rtn;
			
		} catch (SQLException e) {
			return null;
		}		
	}
	
}
