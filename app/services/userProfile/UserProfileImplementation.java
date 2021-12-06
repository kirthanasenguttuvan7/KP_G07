package services.userProfile;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import play.libs.ws.WSClient;
import model.UserProfileModel;
import play.libs.ws.WSResponse;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
public class UserProfileImplementation implements UserProfileApi{
	
	private final WSClient ws;
	
	 /**
	  * The constructor injects the necessary dependancies for the class
	  * @param ws
	  */
	@Inject
	UserProfileImplementation(WSClient ws){
		this.ws = ws;
	}
   

   /**
    * Renders the searched keyword with it's username, repositories and topics
    * @param keyword
    * @return
    */
	@Override
	public CompletionStage<WSResponse> getUserProfile(String username){
		System.out.print("---------------------------" + username + "----------------------");
		return ws.url("https://api.github.com/users/" + username)
                .get(); // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
          
	}
	
}
