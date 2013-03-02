package quiz.messaging;

import java.sql.Connection;

import quiz.base.DBConnector;

public class MessageManager {
	private static Connection db;

	static {
		db = DBConnector.getConnection();
	}
	
	
}
