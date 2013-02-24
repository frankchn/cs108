package quiz.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import quiz.base.DBConnector;

public class Quiz {

	private static Connection db;	
	static {
		db = DBConnector.getConnection();
	}
	
	public final int quiz_id;
	public String name;
	public String description;
	public int user_id;
	public boolean is_public;
	public Timestamp created;
	
	// quiz options
	public boolean random_questions;
	public boolean multiple_pages;
	public boolean immediate_correction;
	public boolean practice_mode;
	
	// Do not randomly initialize this!
	public Quiz(int quiz_id, String name, String description, int user_id, Timestamp created, boolean is_public, boolean random_questions,
			     boolean multiple_pages, boolean immediate_correction, boolean practice_mode) {
		
		this.quiz_id = quiz_id;
		this.name = name;
		this.description = description;
		this.user_id = user_id;
		this.created = created;
		this.is_public = is_public;
		
		this.random_questions = random_questions;
		this.multiple_pages = multiple_pages;
		this.immediate_correction = immediate_correction;
		this.practice_mode = practice_mode;
		
	}
	
	public static Quiz getQuizFromResultSet(ResultSet r) {
		try {
			return new Quiz(r.getInt("quiz_id"),
							r.getString("name"),
							r.getString("description"),
							r.getInt("user_id"),
							r.getTimestamp("created"),
							r.getInt("is_public") == 1 ? true : false,
							r.getInt("random_questions") == 1 ? true : false,
							r.getInt("multiple_pages") == 1 ? true : false,
							r.getInt("immediate_correction") == 1 ? true : false,
							r.getInt("practice_mode") == 1 ? true : false);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static Quiz getQuiz(int quiz_id) {
		try {
			ResultSet r = db.prepareStatement("SELECT * FROM `quiz` WHERE `quiz_id` = " + quiz_id).executeQuery();
			if(!r.next()) return null;
			return getQuizFromResultSet(r);
		} catch (SQLException e) {
			return null;
		}
	}

	public void save() {
		try {
			PreparedStatement p = db.prepareStatement("UPDATE `quiz` SET `name` = ?, `description` = ?, `random_questions` = ?, `multiple_pages` = ?, `immediate_correction` = ?, `practice_mode` = ? WHERE `quiz_id` = ?");
			p.setString(1, this.name);
			p.setString(2, this.description);
			p.setInt(3, this.random_questions ? 1 : 0);
			p.setInt(4, this.multiple_pages ? 1 : 0);
			p.setInt(5, this.immediate_correction ? 1 : 0);
			p.setInt(6, this.practice_mode ? 1 : 0);
			p.setInt(7, this.quiz_id);
			p.executeUpdate();	
		} catch (SQLException e) {
			return;
		}
	}
	
}
