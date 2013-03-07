package quiz.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import quiz.base.DBConnector;

public class Announcement {

	public final int announcement_id;
	public int user_id;
	public Timestamp posted;
	public String subject;
	public String body;
	
	private static Connection db;
	static {
		db = DBConnector.getConnection();
	}
	
	public Announcement(int announcement_id) {
		this.announcement_id = announcement_id;
		
		try {
			ResultSet r = db.prepareStatement("SELECT * FROM `announcement` WHERE `announcement_id` = " + announcement_id).executeQuery();
			if(!r.next()) throw new RuntimeException("Invalid announcement id!");
			
			user_id = r.getInt("user_id");
			posted = r.getTimestamp("posted");
			subject = r.getString("subject");
			body = r.getString("body");
		} catch (SQLException e) { }
	}
	
	public static Announcement[] getAnnouncements() {
		try {
			ResultSet r = db.prepareStatement("SELECT `announcement_id` FROM `announcement` ORDER BY `posted` DESC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery();
			
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			Announcement[] rtn = new Announcement[size];
			for(int i = 0; i < size; i++) {
				r.next();
				rtn[i] = new Announcement(r.getInt("announcement_id"));
			}
			
			return rtn;
		} catch (SQLException e) { return null; }
	}
	
	public static void newAnnouncement(User u, String subject, String body) {
		try {
			PreparedStatement p = db.prepareStatement("INSERT INTO `announcement` (`user_id`, `posted`, `subject`, `body`) VALUES (?, ?, ?, ?)");
			p.setInt(1, u.user_id);
			p.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			p.setString(3, subject);
			p.setString(4, body);
			
			p.executeUpdate();
		} catch (SQLException e) { }
	}
	
}
