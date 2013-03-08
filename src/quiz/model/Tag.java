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
	public int numOccurences;
	public int fontSize;
	
	private static Connection db;
	static {
		db = DBConnector.getConnection();
	}
	
	public Tag(String hashtag) {
		this.hashtag = hashtag;
		this.plaintext = stripHash(hashtag);
		this.numOccurences = getNumOccurences();
		this.fontSize = setFontSize();
	}
	
	//set to a number between 11 and 30
	public int setFontSize() {
		int size = 11 + (numOccurences * 2);
		if (size > 30) {
			size = 30;
		}
		return size;
	}
	
	public int getNumOccurences() {
		try {
			PreparedStatement p = db.prepareStatement("SELECT * from `tag` WHERE `hashtag` = ?");
			p.setString(1, hashtag);
			ResultSet r = p.executeQuery();
			r.last();
			int size = r.getRow();
			return size;
		} catch (SQLException ignored) {
			return 0;
		}			
	}
	
	public String stripHash(String hashtag) {
		return hashtag.substring(1);
	}
	
	// hardcoded numbers for aesthetics
	private static List<List<Tag> > formatTo2D(Tag[] tags) {
		int size = tags.length;
		List<List<Tag> > final_list = new ArrayList<List<Tag> >();
		List<Tag> tag_row = new ArrayList<Tag>();
		for (int i = 0; i < size; i++) {
			if (i % 9 == 8) {
				// start new list
				final_list.add(tag_row);
				tag_row = new ArrayList<Tag>();
			} 
			tag_row.add(tags[i]);
		}
		final_list.add(tag_row);
		return final_list;		
	}
	
	public static List<List<Tag> > get2DArrayOfTags() {
		try {
			ResultSet r = db.prepareStatement("SELECT DISTINCT(hashtag) from `tag`").executeQuery();
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			int i = 0;
			Tag[] rtn = new Tag[size];
			while(r.next()) {
				Tag nextTag = new Tag(r.getString(1));
				rtn[i++] = nextTag;
			}	
			return formatTo2D(rtn);
		} catch (SQLException ignored) {
			return null;
		}		
	}
	
	public static Tag[] getTags() {
		try {
			ResultSet r = db.prepareStatement("SELECT DISTINCT(hashtag) from `tag`").executeQuery();
			r.last();
			int size = r.getRow();
			r.beforeFirst();
			
			int i = 0;
			Tag[] rtn = new Tag[size];
			while(r.next()) {
				Tag nextTag = new Tag(r.getString(1));
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