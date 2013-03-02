package quiz.messaging;

import java.sql.Timestamp;

public class FriendRequest {
	public int friend_request_id;
	public int requestor_user_id;
	public int requestee_user_id;
	public Timestamp request_time;
	
	public FriendRequest(int friend_id, int requestor_id, int requestee_id, Timestamp time) {
		friend_request_id = friend_id;
		requestor_user_id = requestor_id;
		requestee_user_id = requestee_id;
		request_time = time;
	}
}
