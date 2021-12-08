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
import model.Repositories;

import akka.actor.*;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import services.HomeControllerService;

import static akka.pattern.Patterns.ask;
import services.userProfile.*;
import actors.UserProfileActor;
import actors.Messages;
/**
 * This controller contains an action to handle HTTP requests
 * to the user profile page.
 * 
 * @author Sayali Kulkarni 
 */
public class UserProfileController extends Controller {
	
	//final ActorRef userProfileActor;
	UserProfileService userProfile;
    private final WSClient ws;
	List<Repositories> repos = new ArrayList<Repositories>();

	private HttpExecutionContext httpExecutionContext;
	
	/**
	 * The constructor injects the necessary dependencies for the class
	 * @param ws
	 * @param httpExecutionContext
	 */
	@Inject
	public UserProfileController(WSClient ws,  UserProfileService userProfile, HttpExecutionContext httpExecutionContext){
		this.ws = ws;
		//this.userProfileActor = system.actorOf(UserProfileActor.getProps());
		this.userProfile = userProfile;
		this.httpExecutionContext = httpExecutionContext;
	}
    
	/**
	 * It renders the User Profile details for a particular user
	 * @param username
	 * @param repositories
	 * @return
	 */
    public CompletionStage<Result> userProfile(String username, String repositories){
		//return FutureConverters.toJava(
				//ask(userProfileActor, new Messages.UserProfile(username), 5000))
    	return userProfile.getUserProfileService(username)
    	.thenApplyAsync(result -> {
                		UserProfileModel userProfileModel = (UserProfileModel) result;
                    	List<String> al = new ArrayList<String>();
                    	al = Arrays.asList(repositories.split(","));
        				return ok(UserProfile.render(userProfileModel, al));
                }, httpExecutionContext.current());
    	
    }
    
    /**
     * It fetches the repositories for the particular user
     * @param username
     * @return
     */
    public CompletionStage<Result> getUserRepos(String username){
    	return userProfile.getRepos(username)
		.thenApplyAsync(repos -> {
				List<String> repoStrings = new ArrayList<String>();
				for(Repositories repo: repos) {
					repoStrings.add(repo.getFull_name().toString());
				}
				String repoArray = String.join(",", repoStrings);
				return redirect(routes.UserProfileController.userProfile(username, repoArray));	
		}, httpExecutionContext.current());

    }
}
