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
		System.out.println("prepare");
		try {
			PreparedStatement p = db.prepareStatement("SELECT COUNT (*) as count FROM 'quiz' where user_id = ?");
			p.setInt(1, user_id);
			r = p.executeQuery();
			int numQuizzes = r.getInt("count");
			switch (numQuizzes) {
				case 1: saveAchievement(user_id, AMATEUR_AUTHOR);
					System.out.println("saved");
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
	
	public static void saveAchievement(int user_id, int achievement_id) {
		ResultSet r;
		try {
			PreparedStatement p = db.prepareStatement("INSERT INTO `achievement` (`user_id`, `achievement_id`) VALUES (?, ?)");
			p.setInt(1, user_id);
			p.setInt(2, achievement_id);
			r = p.executeQuery();
			
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
			default:
				img = "";
				description = "";
		}
	}
	
}

