package quiz.model;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class QuestionResponse extends QuizQuestion {

	private static final long serialVersionUID = -2765592090894073244L;

	public String question_text = "Untitled Question";
	public String question_image = "";
	public Map<String, Double> correct_answers = new HashMap<String, Double>();
	
	public QuestionResponse(int quiz_question_id, int quiz_id) {
		super(quiz_question_id, quiz_id);
	}
	
	@Override
	public String getTitle() {
		if(question_text.length() > 0)
			return question_text;
		return question_image;
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

	@Override
	public String getFriendlyType() {
		return "Question/Picture Response";
	}

}
