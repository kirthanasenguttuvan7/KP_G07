package controllers.RepositoryProfileController;

import java.util.concurrent.CompletionStage;

import play.data.Form;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import views.html.UserProfileView.*;
import views.html.RepositoryProfileView.*;
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
import model.SearchModel;
import model.RepositoryIssuesModel;


/**
 * This controller contains an action to handle HTTP requests
 * to the user profile page.
 */
public class RepositoryProfileController extends Controller {

	private final WSClient ws;
List<Repositories> repos = new ArrayList<Repositories>();
	List<RepositoryIssuesModel> issues=new ArrayList<RepositoryIssuesModel>();

	private HttpExecutionContext httpExecutionContext;
	
	@Inject
	public RepositoryProfileController(WSClient ws, HttpExecutionContext httpExecutionContext){
		this.ws = ws;
		this.httpExecutionContext = httpExecutionContext;
	}
    /**
     * An action that renders an HTML page displaying .
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    
    public CompletionStage<Result> repositoryProfile(String organization, String reponame){
    	
		return ws.url("https://api.github.com/repos/"+organization+"/"+reponame)
                .get() // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
                .thenApplyAsync(result -> {
                    try {
                    	JsonNode rootNode = result.asJson();
                    	ObjectMapper objectMapper = new ObjectMapper();
                       Repositories repositoryProfile = objectMapper.readValue(rootNode.toString(),Repositories.class);
        				return ok(RepositoryProfile.render(repositoryProfile,reponame));
                    }
                    catch(Exception e) {
                    	return ok(e.toString());
                    }
                }, httpExecutionContext.current());
    	
    }
    
    public CompletionStage<Result> getRepoIssues(String organization, String reponame){
    	return ws.url("https://api.github.com/repos/" + organization+"/"+reponame+"/issues")
		.get()
		.thenApplyAsync(resultIssues -> {
			try {
//				ObjectMapper objectMapper = new ObjectMapper();
//                JsonNode rootNode = resultIssues.asJson();
//                SearchModel searchResult = objectMapper.readValue(rootNode.toString(), SearchModel.class);
//                List<RepositoryIssuesModel> issues = searchResult.getItems();
//                return ok(RepositoryProfile.render(issues));
//            }
//            catch(Exception e) {
//                return ok(e.toString());
				
				
				
				JsonNode rootNode1 = resultIssues.asJson();
				ObjectMapper objectMapper = new ObjectMapper();
				repos = Arrays.asList(objectMapper.treeToValue(rootNode1,
						Repositories[].class));
				List<String> issueStrings = new ArrayList<String>();
				for(Repositories issue: repos) {
					issueStrings.add(issue. getIssues_url().toString());
				}
				String issueArray = String.join(",", issueStrings);
				return redirect(routes.RepositoryProfileController.repositoryProfile(reponame, issueArray));	
			}
			catch(Exception e) {
				return ok(e.toString());
		}
		
		}, httpExecutionContext.current());

    }
}
