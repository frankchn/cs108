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
import java.util.StringTokenizer;

import quiz.base.DBConnector;

public class Tag {
	public String hashtag;
	public String plaintext;
	
	private static Connection db;
	static {
		db = DBConnector.getConnection();
	}
	
	public Tag(String hashtag) {
		this.hashtag = hashtag;
		this.plaintext = stripHash(hashtag);
	}
	
	public String stripHash(String hashtag) {
		return hashtag.substring(1);
	}
	
	public static Tag[] getTags() {
		try {
			ResultSet r = db.prepareStatement("SELECT * from `tag`").executeQuery();
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			int i = 0;
			Tag[] rtn = new Tag[size];
			while(r.next()) {
				Tag nextTag = new Tag(r.getString("hashtag"));
				rtn[i++] = nextTag;
			}	
			return rtn;
		} catch (SQLException ignored) {
			return null;
		}
	}
	
	public static void tagQuiz(int quiz_id, String raw_tags) {
		// Remove existing tags
		try {
			db.prepareStatement("DELETE from `tag` WHERE `quiz_id`=" + quiz_id).executeUpdate();
		} catch (SQLException e) {
			
		}
		List<Tag> tags = parseTags(raw_tags);
		for (int i = 0; i < tags.size(); i++) {
			try {
				PreparedStatement p = db.prepareStatement("INSERT into `tag` VALUES(?, ?)");
				p.setInt(1, quiz_id);
				p.setString(2, tags.get(i).hashtag);
				p.executeUpdate();
			} catch (SQLException e) {
				
			}
		}
	}
	
	/* String is passed in with the form "#tag1 #tag2 #tag3..." */
	private static List<Tag> parseTags(String raw_tags) {
		StringTokenizer tk = new StringTokenizer(raw_tags);
		List<Tag> tag_list = new ArrayList<Tag>();
		while (tk.hasMoreTokens()) {
			String next_token = tk.nextToken();
			if (next_token.indexOf('#') == 0) {
				Tag new_tag = new Tag(next_token);
				tag_list.add(new_tag);
			}
		}
		return tag_list;
	}
}