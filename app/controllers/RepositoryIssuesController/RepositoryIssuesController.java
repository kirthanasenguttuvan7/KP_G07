package controllers.RepositoryIssuesController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;

import Helper.WordStats;
import model.RepositoryIssuesModel;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.RepositoryIssuesView.*;
/**
 * This controller contains an action to handle HTTP requests
 * to the user profile page.
 * * 
 * @author Kirthana Senguttuvan 
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
                    	List<String> titles = new ArrayList<>();
                    	issueModel.forEach(items->{
                    		titles.add(items.getIssue_title());
                    		
                    	});
                    	WordStats stats = new WordStats();
                    	Map<String, Integer> finalStats = new HashMap<String, Integer>();
                    	finalStats = stats.countWords(titles);
                    	StringBuilder htmlBuilder = new StringBuilder();
                    	htmlBuilder.append("<table border = \"1\">");

                    	for (Map.Entry<String, Integer> entry : finalStats.entrySet()) {
                    	    htmlBuilder.append(String.format("<tr><td>%s</td><td>%d</td></tr>",
                    	            entry.getKey(), entry.getValue()));
                    	}

                    	htmlBuilder.append("</table >");

                    	String html = htmlBuilder.toString();
                    	return ok(RepositoryIssues.render(html));
                    }
                    catch(Exception e) {
                    	return ok(e.toString());
                    }
                }, httpExecutionContext.current());
}
}
