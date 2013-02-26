package quiz.model;

import java.util.*;

public class MultipleChoice extends QuizQuestion {

	private static final long serialVersionUID = 847822632399327911L;

	public String question_text = "";
	public int correct_visible = 1;
	public int incorrect_visible = 3;
	public boolean multi_select = false;
	
	public Map<String, Double> correct_options = new HashMap<String, Double>();
	public Map<String, Double> incorrect_options = new HashMap<String, Double>();
	
	public class MultipleChoiceQuestionAttempt extends QuizQuestionAttempt {

		private static final long serialVersionUID = -3361013437292875739L;

		public Set<String> answer = new HashSet<String>();
		
		public Map<String, Double> correct_options = new HashMap<String, Double>();
		public Map<String, Double> incorrect_options = new HashMap<String, Double>();
		
		protected MultipleChoiceQuestionAttempt(int quiz_attempt_id,
				int quiz_question_id, int quiz_id, int user_id) {
			super(quiz_attempt_id, quiz_question_id, quiz_id, user_id);
		}

		@Override
		public void saveAnswer(Map<String, String[]> ans) {
			answer.clear();
			String[] selectedbox = ans.get("qqa_" + quiz_attempt_question_id);
			if(selectedbox != null) {
				// allows spoofing (even though radio qns should only have one selected, we can allow many here) 
				// we don't really check for that :p
				
				for(String opt : selectedbox) 
					answer.add(opt);
			}
			
			save();
		}

		@Override
		public void gradeAnswer() {
			score = 0.0;
			
			graded = QuizQuestionAttemptGraded.done;
			
			for(String opt : answer) {
				if(correct_options.containsKey(opt))
					score += correct_options.get(opt);
				if(incorrect_options.containsKey(opt))
					score += incorrect_options.get(opt);
			}
			
			save();
		}
		
	}
	
	public MultipleChoice(int quiz_question_id, int quiz_id) {
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
		correct_visible = response.get("correct_visible")[0].length() > 0 ?
						  Integer.parseInt(response.get("correct_visible")[0]) : 1;
		incorrect_visible = response.get("incorrect_visible")[0].length() > 0 ?
				  			Integer.parseInt(response.get("incorrect_visible")[0]) : 1;
						  			
		multi_select = response.get("multi_select") != null;
				
		correct_options.clear();
		incorrect_options.clear();
		
		// Parse options
		String[] correct_answer_key = response.get("correct_answer_key");
		String[] correct_answer_score  = response.get("correct_answer_score");

		String[] incorrect_answer_key = response.get("incorrect_answer_key");
		String[] incorrect_answer_score  = response.get("incorrect_answer_score");
		
		if(correct_answer_key != null) {
			for(int i = 0; i < correct_answer_key.length; i++) {
				if(correct_answer_key[i].length() > 0 && correct_answer_score[i].length() > 0)
					correct_options.put(correct_answer_key[i], Double.parseDouble(correct_answer_score[i]));
			}
		}
		
		if(incorrect_answer_key != null) {
			for(int i = 0; i < incorrect_answer_key.length; i++) {
				if(incorrect_answer_key[i].length() > 0 && incorrect_answer_score[i].length() > 0)
					incorrect_options.put(incorrect_answer_key[i], Double.parseDouble(incorrect_answer_score[i]));
			}
		}
		
		save();
	}

	@Override
	public String getFriendlyType() {
		return "Multiple Choice";
	}

	@Override
	public QuizQuestionAttempt newQuizQuestionAttempt(QuizAttempt attempt, User user) {
		MultipleChoiceQuestionAttempt qqa = 
				new MultipleChoiceQuestionAttempt(attempt.quiz_attempt_id,
												  quiz_question_id,
												  quiz_id,
												  user.user_id);
		
		Random rand = new Random();
		
		int cv = Math.min(this.correct_visible, this.correct_options.size());
		int iv = Math.min(this.incorrect_visible, this.incorrect_options.size());
		
		System.out.println(cv + " " + iv);
		
		HashMap<String, Double> cvm = new HashMap<String, Double>();
		HashMap<String, Double> ivm = new HashMap<String, Double>();
		
		cvm.putAll(this.correct_options);
		ivm.putAll(this.incorrect_options);
		
		while(qqa.correct_options.size() < cv) {
			String random_key;
			if(cvm.keySet().size() == 1)
				random_key = (String) cvm.keySet().toArray()[0];
			else
				random_key = (String) cvm.keySet().toArray()[rand.nextInt(cvm.keySet().size() - 1)];
			qqa.correct_options.put(random_key, cvm.get(random_key));
			cvm.remove(random_key);
		}
		
		while(qqa.incorrect_options.size() < iv) {
			String random_key;
			if(ivm.keySet().size() == 1)
				random_key = (String) ivm.keySet().toArray()[0];
			else
				random_key = (String) ivm.keySet().toArray()[rand.nextInt(ivm.keySet().size() - 1)];
			qqa.incorrect_options.put(random_key, ivm.get(random_key));
			ivm.remove(random_key);
		}
		
		qqa.save();
		
		return qqa;
	}

}
