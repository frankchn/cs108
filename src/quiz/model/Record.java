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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import quiz.base.DBConnector;

public class Record {
	public int quiz_id;
	public int user_id;
	public QuizAttempt[] attempts;
	public String quiz_name;
	public int rating;
	public Timestamp last_start_time;
	
	private static Connection db;
	static {
		db = DBConnector.getConnection();
	}
	
	public Record(int user_id, int quiz_id, Timestamp last_start_time) {
		this.quiz_id = quiz_id;
		this.user_id = user_id;
		this.quiz_name = getQuizName(quiz_id);
		this.rating = getRating(quiz_id, user_id);
		this.last_start_time = last_start_time;
		
		//attempts = QuizAttempt.loadAttemptsForRecord(quiz_id, user_id);
	}
	
	private String getQuizName(int quiz_id) {
		ResultSet r;
		try {
			PreparedStatement p = db.prepareStatement("SELECT * FROM `quiz` where `quiz_id` = ?");
			p.setInt(1, quiz_id);
			r = p.executeQuery();
			if (!r.next())
				return null;
			return r.getString("name");
		} catch (SQLException ignored) { 
			return null;
		}
	}
	
	private int getRating(int quiz_id, int user_id) {
		ResultSet r;
		try {
			PreparedStatement p = db.prepareStatement("SELECT * FROM `rating` where `quiz_id` = ? AND `user_id` = ?");
			p.setInt(1, quiz_id);
			p.setInt(2, user_id);
			r = p.executeQuery();
			if (!r.next())
				return 0;
			return r.getInt("stars");
		} catch (SQLException ignored) {
			return 0;
		}
	}
	
	
}

