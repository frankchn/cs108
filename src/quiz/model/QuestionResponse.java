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
	
	public class QuestionResponseQuestionAttempt extends QuizQuestionAttempt {
		
		private static final long serialVersionUID = 5064105115168916881L;
		public String answer = "";
		
		protected QuestionResponseQuestionAttempt(int quiz_attempt_id, 
												  int quiz_question_id, 
												  int quiz_id,
												  int user_id) {
			super(quiz_attempt_id, quiz_question_id, quiz_id, user_id);
			answer = "";
			save();
		}

		@Override
		public double getScore() {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
	
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
		String[] answer_key = response.get("correct_answer_key");
		String[] answer_score  = response.get("correct_answer_score");
		
		question_text = response.get("question_text")[0];
		question_image = response.get("question_image")[0];
		
		correct_answers.clear();
		
		if(answer_key != null) {
			for(int i = 0; i < answer_key.length; i++) {
				if(answer_key[i].length() > 0 && answer_score[i].length() > 0)
					correct_answers.put(answer_key[i], Double.parseDouble(answer_score[i]));
			}
		}
		
		save();
	}

	@Override
	public String getFriendlyType() {
		return "Question/Picture Response";
	}

	@Override
	public QuizQuestionAttempt newQuizQuestionAttempt(QuizAttempt attempt, User user) {
		QuestionResponseQuestionAttempt qqa = 
			new QuestionResponseQuestionAttempt(attempt.quiz_attempt_id,
												quiz_question_id,
												quiz_id,
												user.user_id);
		return qqa;
	}

}
