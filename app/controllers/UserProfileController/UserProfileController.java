package controllers.UserProfileController;

import java.util.concurrent.CompletionStage;

import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import views.html.UserProfileView.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import play.libs.concurrent.HttpExecutionContext;
import javax.inject.Inject;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import model.UserProfileModel;
/**
 * This controller contains an action to handle HTTP requests
 * to the user profile page.
 */
public class UserProfileController extends Controller {

	private final WSClient ws;
	private HttpExecutionContext httpExecutionContext;
	
	@Inject
	UserProfileController(WSClient ws, HttpExecutionContext httpExecutionContext){
		this.ws = ws;
		this.httpExecutionContext = httpExecutionContext;
	}
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    
    public CompletionStage<Result> userProfile(String username){
    	
		//return ok(UserProfile.render(username));
		return ws.url("https://api.github.com/users/defunkt")
                .get() // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
                .thenApplyAsync(result -> {
                    try {
                    	JsonNode rootNode = result.asJson();
                    	ObjectMapper objectMapper = new ObjectMapper();
                        UserProfileModel userProfile = objectMapper.readValue(rootNode.toString(), UserProfileModel.class);
                        System.out.println(userProfile.getLogin());
                        return ok(UserProfile.render(userProfile));
                    }
                    catch(Exception e) {
                    	return ok(e.toString());
                    }
                }, httpExecutionContext.current());
    	
    }

}
