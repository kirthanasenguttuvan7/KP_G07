package controllers.UserProfileController;

import java.util.concurrent.CompletionStage;

import play.mvc.*;
import views.html.UserProfileView.*;
/**
 * This controller contains an action to handle HTTP requests
 * to the user profile page.
 */
public class UserProfileController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok("Welcome to github");
    }
    
    public Result userProfile(String message){
		return ok(UserProfile.render(message));
    	
    }

}
