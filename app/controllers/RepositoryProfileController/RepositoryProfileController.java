package controllers.RepositoryProfileController;

import java.util.concurrent.CompletionStage;

import scala.util.parsing.json.JSONArray;
import services.userProfile.UserProfileService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import play.libs.ws.*;
import play.data.Form;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import views.html.UserProfileView.*;
import views.html.RepositoryProfileView.*;
import views.html.RepositoryProfileView.topIssues.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static java.util.stream.Collectors.toList;
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
import model.ContributionsModel;
import model.RepositoryIssuesUserModel;
import Helper.RepositoryIssueHelper;
import model.Issues;
import play.libs.Json;
import services.repositoryProfile.*;
import java.util.stream.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the user profile page.
 * 
 * @author Sree Pooja Chandupatla 
 */
public class RepositoryProfileController extends Controller {

	private final WSClient ws;
	RepositoryProfileService repositoryProfile;
List<Repositories> repos = new ArrayList<Repositories>();
	List<RepositoryIssuesModel> issues=new ArrayList<RepositoryIssuesModel>();
	List<ContributionsModel> collaborators=new ArrayList<ContributionsModel>();
	List<RepositoryIssuesModel> i=new ArrayList<RepositoryIssuesModel>();

	private HttpExecutionContext httpExecutionContext;
	
	@Inject
	RepositoryProfileController(WSClient ws, RepositoryProfileService repositoryProfile, HttpExecutionContext httpExecutionContext){
		this.ws = ws;
		this.repositoryProfile = repositoryProfile;
		this.httpExecutionContext = httpExecutionContext;
	}
    /**
     * An action that renders an HTML page displaying .
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    
    public CompletionStage<Result> getrepositoryProfile(String reponame,String organization, String collabArray){
    	
    	return repositoryProfile.getRepoProfileService(organization,reponame,collabArray)
                 // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
                .thenApplyAsync(result -> {
                    try {
                    	
                       
                       //helper.getRepositoryIssueDetails(organization, reponame);
                       
        				return ok(RepositoryProfile.render(result,reponame,collabArray));
                    }
                    catch(Exception e) {
                    	return ok(e.toString());
                    }
                }, httpExecutionContext.current());
    	
    }
    public CompletionStage<Result> getRepoIssues(String organization, String reponame) {
    	return repositoryProfile.getReposIssuesService(organization,reponame)
		.thenApply(array -> {
			List<Issues> issues = RepositoryIssueHelper.getIssues(array);
			List<Issues> top20Issues = issues.stream()
					.sorted(Comparator.comparing(Issues::getCreated_at).reversed()).limit(20)
					.collect(toList());
			JsonNode issuesJson=Json.toJson(top20Issues);
			//System.out.println(issuesJson);
			return ok(topIssues.render(organization,reponame,top20Issues));
		});
	}

    
    

    
public CompletionStage<Result> getCollaborators(String organization,String reponame ){
	return repositoryProfile.getRepositoryProfileService(organization,reponame)
                .thenApplyAsync(collaborators -> {
                    try {
                    	
                    List<String> collabSt = new ArrayList<String>();
    				for(ContributionsModel collab: collaborators) {
    					collabSt.add(collab.getLogin().toString());
    					
    				}
    				String collabArray = String.join(",", collabSt);
    				return redirect(routes.RepositoryProfileController.getrepositoryProfile(reponame,organization, collabArray));	
    			}
    			catch(Exception e) {
    				return ok(e.toString());
    		}
                }, httpExecutionContext.current());
    	
    }
}

