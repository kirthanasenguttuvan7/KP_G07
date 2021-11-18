package controllers.RepositoryIssuesController;

import java.util.concurrent.CompletionStage;
import Helper.WordStats;
import java.util.concurrent.ExecutionException;

import play.mvc.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.TypeKey;

import views.html.RepositoryIssuesView.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import play.api.libs.json.Json;
import play.libs.concurrent.HttpExecutionContext;
import javax.inject.Inject;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import model.RepositoryIssuesModel;
import model.UserProfileModel;
/**
 * This controller contains an action to handle HTTP requests
 * to the user profile page.
 */
public class RepositoryIssuesController extends Controller {

	private final WSClient ws;
	private HttpExecutionContext httpExecutionContext;
	private String issue_number;
	private String issue_title;
	private String state;
	private String created_at;
	private String updated_at;
	
	@Inject
	RepositoryIssuesController(WSClient ws, HttpExecutionContext httpExecutionContext){
		this.ws = ws;
		this.httpExecutionContext = httpExecutionContext;
	}
	/**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    
    public CompletionStage<Result> repositoryIssues(String username, String repositoryName){
    	System.out.println(username+ " - "+ repositoryName);
		return ws.url("https://api.github.com/repos/" + username+"/"+repositoryName+"/"+"issues")
				.get() // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
                .thenApplyAsync(result -> {
                    try {
                    	JsonNode rootNode = result.asJson();
                    	List<RepositoryIssuesModel> issueModel = new ArrayList<>(); 
                    	rootNode.forEach(items -> {
                			 String issue_number = items.get("number").toString();
                			 String issue_title = items.get("title").toString();
                			 String state = items.get("state").toString();
                			 String created_at = items.get("created_at").toString();
                			 String updated_at = items.get("updated_at").toString();
                			 issueModel.add(new RepositoryIssuesModel(issue_number,issue_title,state,created_at,updated_at));
                		 });
//                    	System.out.println(issueModel.get(0).getIssue_title());
                    	List<String> titles = new ArrayList<>();
                    	issueModel.forEach(items->{
                    		titles.add(items.getIssue_title());
                    	});
                    	WordStats stats = new WordStats();
                    	Map<String, Integer> finalStats = new HashMap<String, Integer>();
                    	finalStats = stats.countWords(titles);
                    	for(String item :finalStats.keySet()) {
                    		System.out.println(item.toString()+"-"+finalStats.get(item).toString());
                    	}
                    	String yourMap = Json.stringify(Json.toJson(finalStats));
                    	return ok(RepositoryIssues.render());
                    }
                    catch(Exception e) {
                    	return ok(e.toString());
                    }
                }, httpExecutionContext.current());
}
}
