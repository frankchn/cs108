package quiz.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;

import quiz.base.DBConnector;

public abstract class QuizQuestion implements Serializable {

	private static final long serialVersionUID = 5346847141930800854L;
	protected static Connection db = DBConnector.getConnection();;	
	
	public abstract class QuizQuestionAttempt implements Serializable {
		private static final long serialVersionUID = 8116729402810794552L;
		
		public final int quiz_attempt_question_id;
		public final int quiz_attempt_id;
		public final int quiz_question_id;
		public final int quiz_id;
		public final int user_id;
		public double score = 0.0;
		public QuizQuestionAttemptGraded graded = QuizQuestionAttemptGraded.undone;
		
		public QuizQuestion getQuizQuestion() {
			return QuizQuestion.this;
		}
		
		public abstract void saveAnswer(Map<String, String[]> ans);
		public abstract void gradeAnswer();
		
		protected QuizQuestionAttempt(int quiz_attempt_id,
									  int quiz_question_id,
									  int quiz_id,
									  int user_id) {
			
			int qaq_id = -1;
			this.quiz_attempt_id = quiz_attempt_id;
			this.quiz_question_id = quiz_question_id;
			this.user_id = user_id;
			this.quiz_id = quiz_id;
			
			PreparedStatement ps;
			try {
				ps = db.prepareStatement("INSERT INTO `quiz_attempt_question` (`quiz_attempt_id`, `quiz_question_id`, `quiz_id`, `user_id`) VALUES (?, ?, ?, ?)",
										 Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, quiz_attempt_id);
				ps.setInt(2, quiz_question_id);
				ps.setInt(3, quiz_id);
				ps.setInt(4, user_id);
				ps.executeUpdate();
				
				ResultSet s = ps.getGeneratedKeys();
				s.next();
				qaq_id = s.getInt(1);
			} catch (SQLException ignored) {  }
			
			this.quiz_attempt_question_id = qaq_id;
			
			save();
		}
		
		public void save() {
			try {
				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				ObjectOutputStream os = new ObjectOutputStream(bs);
				os.writeObject(this);
				os.close();
				
				PreparedStatement ps = db.prepareStatement("UPDATE `quiz_attempt_question` SET `metadata` = ?, `graded` = ? WHERE `quiz_attempt_question_id` = ?");
				ps.setObject(1, bs.toByteArray());
				ps.setString(2, graded.toString());
				ps.setInt(3, this.quiz_attempt_question_id);
							
				ps.executeUpdate();
			} catch (Exception e) {
				throw new RuntimeException("Something went badly wrong");
			}
		}
		
		public double getScore() {
			return score;
		}
	}
	
	public final int quiz_question_id;
	public final int quiz_id;
	
	abstract public String getTitle();
	abstract public QuizQuestionAttempt gradeResponse(Map<String, String[]> response);
	abstract public void updateQuestion(Map<String, String[]> response);
	
	abstract public String getFriendlyType();
	abstract public QuizQuestionAttempt newQuizQuestionAttempt(QuizAttempt attempt, User user);
	
	public void save() {
		try {
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bs);
			os.writeObject(this);
			os.close();
			
			PreparedStatement ps = db.prepareStatement("UPDATE `quiz_question` SET `metadata` = ? WHERE `quiz_question_id` = ?");
			ps.setObject(1, bs.toByteArray());
			ps.setInt(2, this.quiz_question_id);
						
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Something went badly wrong");
		}
	}
	
	public static QuizQuestionAttempt loadQuizQuestionAttempt(int quiz_attempt_question_id) {
		try {
			ResultSet r = db.prepareStatement("SELECT * FROM `quiz_attempt_question` WHERE `quiz_attempt_question_id` = " + quiz_attempt_question_id).executeQuery();
			if(!r.next()) return null;
			return loadQuizQuestionAttempt(r.getObject("metadata"));
		} catch (SQLException e) {
			throw new RuntimeException("Cannot load question attempt.");
		}
	}
	
	public static QuizQuestionAttempt loadQuizQuestionAttempt(Object bas) {
		try {
			ByteArrayInputStream bs = new ByteArrayInputStream((byte[]) bas);
			ObjectInputStream is = new ObjectInputStream(bs);
			return (QuizQuestionAttempt) is.readObject();
		} catch (Exception e) {
			throw new RuntimeException("Something went seriously wrong here. Cannot unserialize.");
		}
	}
	
	public static QuizQuestion loadQuestion(int quiz_question_id) {
		try {
			ResultSet r = db.prepareStatement("SELECT * FROM `quiz_question` WHERE `quiz_question_id` = " + quiz_question_id).executeQuery();
			if(!r.next()) return null;
			return loadQuestion(r.getObject("metadata"));
		} catch (SQLException e) {
			throw new RuntimeException("Cannot load question.");
		}
	}
	
	public static QuizQuestion loadQuestion(Object bas) {
		try {
			ByteArrayInputStream bs = new ByteArrayInputStream((byte[]) bas);
			ObjectInputStream is = new ObjectInputStream(bs);
			return (QuizQuestion) is.readObject();
		} catch (Exception e) {
			throw new RuntimeException("Something went seriously wrong here. Cannot unserialize.");
		}
	}
	
	public static <T> QuizQuestion newQuestion(int quiz_id, Class<T> cl) {
		try {
			int new_order = 0;
			ResultSet rsq = db.prepareStatement("SELECT MAX(`sort_order`) FROM `quiz_question` WHERE `quiz_id` = " + quiz_id).executeQuery();
			if(rsq.next()) 
				new_order = rsq.getInt(1) + 100;
			
			PreparedStatement p = db.prepareStatement("INSERT INTO `quiz_question` (`quiz_id`, `type`, `sort_order`) VALUES (?, ?, ?)", 
													  Statement.RETURN_GENERATED_KEYS);
			p.setInt(1, quiz_id);
			p.setString(2, cl.getSimpleName());
			p.setInt(3, new_order);
			p.executeUpdate();
			
			ResultSet s = p.getGeneratedKeys();
			s.next();
			int new_qqid = s.getInt(1);
			QuizQuestion qrobject;
			
			try {
				qrobject = (QuizQuestion) cl.getConstructor(new Class[]{Integer.TYPE, Integer.TYPE}).newInstance(new_qqid, quiz_id);
				qrobject.save();
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		
			return qrobject;
		} catch (SQLException e) {
			System.out.println(e.toString());
			return null;
		}
	}
	
	public void delete() {
		try {
			db.prepareStatement("DELETE FROM `quiz_question` WHERE `quiz_question_id` = " + quiz_question_id).executeUpdate();
		} catch (SQLException ignored) { }
	}
	
	public void moveUp() {
		throw new RuntimeException("not implemented.");
	}
	
	public void moveDown() {
		throw new RuntimeException("not implemented.");
	}
	
	// do not randomly initialize this!
	public QuizQuestion(int quiz_question_id, int quiz_id) {
		this.quiz_question_id = quiz_question_id;
		this.quiz_id = quiz_id;
	}
	
}
