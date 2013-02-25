package quiz.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
	
	private static Connection db;	
	static {
		db = DBConnector.getConnection();
	}
	
	private QuizAttempt(int quiz_attempt_id, int quiz_id, int user_id) {
		this.quiz_id = quiz_id;
		this.quiz_attempt_id = quiz_attempt_id;
		this.user_id = user_id;
	}
	
	public static QuizAttempt newQuizAttempt(Quiz quiz, User user, boolean show_score) {
		QuizAttempt qa = null;
		
		try {
			PreparedStatement ps = db.prepareStatement("INSERT INTO `quiz_attempt` (`quiz_id`, `user_id`, `start_time`, `show_score`) VALUES (?, ?, ?, ?)");
			ps.setInt(1, quiz.quiz_id);
			ps.setInt(2, user.user_id);
			ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			ps.setInt(4, show_score ? 1 : 0);
			ps.executeUpdate();
			
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
	
}
