package quiz.model;

import java.util.*;

public class FillInTheBlanks extends QuizQuestion {

	private static final long serialVersionUID = -4708089884548419713L;

	public class FillInTheBlanksQuestionAttempt extends QuizQuestionAttempt {

		private static final long serialVersionUID = -3105203143340286069L;
		public Map<Integer, String> answer = new HashMap<Integer, String>();

		protected FillInTheBlanksQuestionAttempt(int quiz_attempt_id,
				int quiz_question_id, int quiz_id, int user_id) {
			super(quiz_attempt_id, quiz_question_id, quiz_id, user_id);
			save();
		}

		@Override
		public void saveAnswer(Map<String, String[]> ans) {
			answer.clear();
			String target_string = "qqa_" + quiz_attempt_question_id + "_";
			for(String key : ans.keySet()) {
				if(key.contains(target_string)) {
					int field_id = Integer.parseInt(key.substring(target_string.length()));
					answer.put(field_id, ans.get(key)[0]);
				}
			}
			save();
		}

		@Override
		public void gradeAnswer() {
			score = 0.0;		
			graded = QuizQuestionAttemptGraded.done;
			
			for(Integer blank_id : answer.keySet()) {
				String current_answer = answer.get(blank_id);

				if(FillInTheBlanks.this.correct_answers.get(blank_id).containsKey(current_answer)) {
					score += FillInTheBlanks.this.correct_answers.get(blank_id).get(current_answer);
				}
			}
			
			save();
		}
		
	}
	
	public String question_text = "";
	public HashMap<Integer, HashMap<String, Double>> correct_answers = 
			new HashMap<Integer, HashMap<String, Double>>();
	
	public FillInTheBlanks(int quiz_question_id, int quiz_id) {
		super(quiz_question_id, quiz_id);
		save();
	}

	@Override
	public String getTitle() {
		return question_text;
	}

	@Override
	public QuizQuestionAttempt gradeResponse(Map<String, String[]> response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateQuestion(Map<String, String[]> response) {
		question_text = response.get("question_text")[0];
		
		correct_answers.clear();
		
		for(int i = 0; i < response.get("correct_answer_blank").length; i++) {
			if(response.get("correct_answer_blank")[i].length() > 0) {
				int question_id = Integer.parseInt(response.get("correct_answer_blank")[i]);
				String ans = response.get("correct_answer_key")[i];
				double sc = Double.parseDouble(response.get("correct_answer_score")[i]);
				
				if(!correct_answers.containsKey(question_id))
					correct_answers.put(question_id, new HashMap<String, Double>());
				
				correct_answers.get(question_id).put(ans, sc);
			}
		}
		
		save();
	}

	@Override
	public String getFriendlyType() {
		return "Fill-in-the-Blanks";
	}

	@Override
	public QuizQuestionAttempt newQuizQuestionAttempt(QuizAttempt attempt,
			User user) {
		return new FillInTheBlanksQuestionAttempt(attempt.quiz_attempt_id,
												  quiz_question_id,
												  quiz_id,
												  user.user_id);
	}

}
