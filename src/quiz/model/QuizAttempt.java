package quiz.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
