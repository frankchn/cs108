package quiz.model;

import java.sql.Connection;
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
	public Date created;
	
	// quiz options
	public boolean random_questions;
	public boolean multiple_pages;
	public boolean immediate_correction;
	public boolean practice_mode;
	
	// Do not randomly initialize this!
	public Quiz(int quiz_id, String name, String description, int user_id, Date created, boolean is_public, boolean random_questions,
			     boolean multiple_pages, boolean immediate_correction, boolean practice_mode) {
		
		this.quiz_id = quiz_id;
		this.name = name;
		this.description = description;
		this.user_id = user_id;
		this.is_public = is_public;
		
		this.random_questions = random_questions;
		this.multiple_pages = multiple_pages;
		this.immediate_correction = immediate_correction;
		this.practice_mode = practice_mode;
		
	}
	
	public static Quiz getQuiz(int quiz_id) {
		return null;
	}
	
}
