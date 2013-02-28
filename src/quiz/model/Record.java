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
	
	public int user_id;
	public String quiz_name;
	public Timestamp submission_time;
	public double score;
	public int is_real;
	public int is_complete;
	
	private static Connection db;
	static {
		db = DBConnector.getConnection();
	}
	
	public Record(int user_id, int quiz_id, Timestamp submission_time, double score, int show_score, int finished) {
		this.user_id = user_id;
		this.submission_time = submission_time;
		this.score = score;
		this.is_real = show_score;
		this.is_complete = finished;
		ResultSet r;
		try {
			PreparedStatement p = db.prepareStatement("SELECT * FROM `quiz` where `quiz_id` = ?");
			p.setInt(1, quiz_id);
			r = p.executeQuery();
			r.next();
			this.quiz_name = r.getString("name");
		} catch (SQLException ignored) { }
		
	}
	
	
}

