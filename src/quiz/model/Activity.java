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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import quiz.base.DBConnector;

public class Activity {
	

	public int user_id;
	public String description;
	public Timestamp time;
	public Quiz quiz;
	
	private static Connection db;
	static {
		db = DBConnector.getConnection();
	}

	
	public Activity(int user_id, String description, Timestamp time, Quiz quiz) {
		this.user_id = user_id;
		this.description = description;
		this.time = time;
		this.quiz = quiz;
	}
	
	public static class CustomComparator implements Comparator<Activity> {
	    @Override
	    public int compare(Activity o1, Activity o2) {
	        return o1.time.compareTo(o2.time);
	    }
	}
	
	public static void sortByTime(List<Activity> act) {
		Collections.sort(act, new CustomComparator());
	}
	
}



