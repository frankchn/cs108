package quiz.model;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import quiz.base.DBConnector;

public class Achievement {
	
	public static final int AMATEUR_AUTHOR = 1;
	public static final int PROLIFIC_AUTHOR = 2;
	public static final int PRODIGIOUS_AUTHOR = 3;
	public static final int QUIZ_MACHINE = 4;
	public static final int GREATEST = 5;
	

	public final int user_id;
	public final int achievement_id;
	public final String description;
	public final String img;

	private static Connection db;
	static {
		db = DBConnector.getConnection();
	}
	
	public static void alertCreateQuiz(int user_id) {
		ResultSet r;
		try {
			PreparedStatement p = db.prepareStatement("SELECT COUNT(*) FROM `quiz` where `user_id` = ?");
			p.setInt(1, user_id);
			r = p.executeQuery();
			r.next();
			int numQuizzes = r.getInt(1);
			switch (numQuizzes) {
				case 1: saveAchievement(user_id, AMATEUR_AUTHOR);
					break;
				case 5: saveAchievement(user_id, PROLIFIC_AUTHOR);
					break;
				case 10: saveAchievement(user_id, PRODIGIOUS_AUTHOR);
					break;
				default:
					break;			
			}
		} catch (SQLException ignored) { }
	}
	
	public static void alertTakeQuiz(int user_id, int quiz_attempt_id, int quiz_id) {
		ResultSet r;
		try {
			PreparedStatement p = db.prepareStatement("SELECT COUNT(*) from `quiz_attempt` where `user_id` = ?");
			p.setInt(1, user_id);
			r = p.executeQuery();
			r.next();
			int numTaken = r. getInt(1);
			if (numTaken == 10) {
				saveAchievement(user_id, QUIZ_MACHINE);
			}
		} catch (SQLException ignored) { }
		
		checkHighestScore(user_id, quiz_attempt_id, quiz_id);
	}
	
	private static void checkHighestScore(int user_id, int quiz_attempt_id, int quiz_id) {
		ResultSet r;
		try {
			PreparedStatement p2 = db.prepareStatement("SELECT * from `quiz_attempt` where `quiz_attempt_id` = ?");
			p2.setInt(1, quiz_attempt_id);
			r = p2.executeQuery();
			r.next();
			double user_score = r.getDouble("score");
			PreparedStatement p3 = db.prepareStatement("SELECT MAX(score) from `quiz_attempt` where `quiz_id` = ?");
			p3.setInt(1, quiz_id);
			r = p3.executeQuery();
			r.next();
			double max_score = r.getDouble(1);
			System.out.println(user_score);
			System.out.println(max_score);
			if (user_score >= max_score && user_score > 0) {
				saveAchievement(user_id, GREATEST);
			}
		} catch (SQLException ignored) { }	
	}
		
	public static void saveAchievement(int user_id, int achievement_id) {
		try {
			PreparedStatement p = db.prepareStatement("INSERT INTO `achievement` (`user_id`, `achievement_id`) VALUES (?, ?)");
			p.setInt(1, user_id);
			p.setInt(2, achievement_id);
			p.executeUpdate();
			
		} catch (SQLException ignored) {  }
	}
	
	public Achievement(int user_id, int achievement_id) {
		this.user_id = user_id;
		this.achievement_id = achievement_id;
		switch (achievement_id) {
			case 1:
				img = "images/badge.gif";
				description = "Amateur Author";
				break;
			case 2:
				img = "images/badge.gif";
				description = "Prolific Author";
				break;
			case 3:
				img = "images/badge.gif";
				description = "Prodigious Author";
				break;
			case 4:
				img = "images/badge.gif";
				description = "Quiz Machine";
				break;
			case 5:
				img = "images/badge.gif";
				description = "You're the Greatest!";
				break;
			default:
				img = "";
				description = "";
		}
	}
	
}

