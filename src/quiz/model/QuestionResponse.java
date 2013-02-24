package quiz.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class QuestionResponse extends QuizQuestion {

	private static final long serialVersionUID = -2765592090894073244L;

	protected boolean textQuestion;
	protected String question_text;
	protected String question_image;
	protected List<String> correct_answers;
	protected double score;
	
	private QuestionResponse(int quiz_question_id, int quiz_id) {
		super(quiz_id, quiz_question_id);
	}
	
	public static QuestionResponse newQuestion(int quiz_id) {
		try {
			int new_order = 0;
			ResultSet rsq = db.prepareStatement("SELECT MAX(`sort_order`) FROM `quiz_question` WHERE `quiz_id` = " + quiz_id).executeQuery();
			if(rsq.next()) 
				new_order = rsq.getInt(1) + 100;
			
			PreparedStatement p = db.prepareStatement("INSERT INTO `quiz_question` (`quiz_id`, `type`, `sort_order`) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			p.setInt(1, quiz_id);
			p.setString(2, "QuestionResponse");
			p.setInt(3, new_order);
			
			return null;
		} catch (SQLException e) {
			return null;
		}
	}
	
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuizQuestionAttempt gradeResponse(Map<String, String[]> response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateQuestion(Map<String, String[]> response) {
		super.save();
	}

}
