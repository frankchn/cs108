package quiz.model;

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
