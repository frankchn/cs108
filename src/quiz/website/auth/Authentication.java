package quiz.website.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quiz.model.User;

public class Authentication {

	public static boolean require_login(HttpServletRequest request, 
			HttpServletResponse response) {
		return require_login(request, response, false);
	}
	
	public static boolean require_login(HttpServletRequest request, 
										HttpServletResponse response, 
										boolean require_admin) {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(currentUser == null) return false;
		if(require_admin && !currentUser.is_admin) return false;
				
		return true;
	}
	
}
