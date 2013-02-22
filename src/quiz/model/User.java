package quiz.model;

public class User {

	public final int user_id;
	public final String name;
	public final String email;
	
	public User(int user_id) {
		this.user_id = user_id;
		this.name = "";
		this.email = "";
	}
	
}
