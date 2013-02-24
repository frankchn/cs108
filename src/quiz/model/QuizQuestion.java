package quiz.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import quiz.base.DBConnector;

public abstract class QuizQuestion implements Serializable {

	private static final long serialVersionUID = 5346847141930800854L;
	private static Connection db;	
	static {
		db = DBConnector.getConnection();
	}
	
	public abstract class QuizQuestionAttempt {
		public final int quiz_attempt_question_id;
		public final int quiz_attempt_id;
		public final int quiz_question_id;
		public final int quiz_id;
		public final int user_id;
		
		private QuizQuestionAttempt(int quiz_attempt_question_id,
									int quiz_attempt_id,
									int quiz_question_id,
									int quiz_id,
									int user_id) {
			this.quiz_attempt_question_id = quiz_attempt_question_id;
			this.quiz_attempt_id = quiz_attempt_id;
			this.quiz_question_id = quiz_question_id;
			this.user_id = user_id;
			this.quiz_id = quiz_id;
		}
		
		abstract public double getScore();
	}
	
	public final int quiz_question_id;
	public final int quiz_id;
	
	abstract public String getTitle();
	abstract public QuizQuestionAttempt gradeResponse(Map<String, String[]> response);
	abstract public void updateQuestion(Map<String, String[]> response);
		
	public void save() {
		try {
			PreparedStatement ps = db.prepareStatement("UPDATE `quiz_question` SET `metadata` = ? WHERE `quiz_question_id` = ?");
			ps.setObject(1, this);
			ps.setInt(2, this.quiz_question_id);
			ps.executeUpdate();
		} catch (SQLException e) {
			return;
		}
	}
	
	public void delete() {
		
	}
	
	public void moveUp() {
		
	}
	
	public void moveDown() {
		
	}
	
	// do not randomly initialize this!
	protected QuizQuestion(int quiz_question_id, int quiz_id) {
		this.quiz_question_id = quiz_question_id;
		this.quiz_id = quiz_id;
	}
	
}
