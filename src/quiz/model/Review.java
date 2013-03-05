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

public class Review {
	public int quiz_id;
	public int user_id;
	public String content;
	public Timestamp time;
	
	private static Connection db;
	static {
		db = DBConnector.getConnection();
	}
	
	public Review(int user_id, int quiz_id, String content, Timestamp time) {
		this.quiz_id = quiz_id;
		this.user_id = user_id;
		this.content = content;
		this.time = time;
	}
	
}

