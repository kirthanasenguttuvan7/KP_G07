package controllers;

import play.mvc.*;
import views.html.*;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import javax.inject.Inject;

 	
import play.libs.ws.WSClient;
import model.SearchModel;
import model.Repositories;
import model.KeywordModel;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

	private final WSClient ws;
	private HttpExecutionContext httpExecutionContext;
	FormFactory formFactory;
	
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
	
	  public Result index() {
		  List<Repositories> repo = new ArrayList<>();
		  return ok(index.render(formFactory.form(KeywordModel.class), repo)); 
	  }
	 
	@Inject
	HomeController(WSClient ws, HttpExecutionContext httpExecutionContext, FormFactory formFactory){
		this.ws = ws;
		this.httpExecutionContext = httpExecutionContext;
		this.formFactory = formFactory;
	}
    
    public CompletionStage<Result> getSearchResult(Http.Request request){
    	
    	Form<KeywordModel> searchForm = formFactory.form(KeywordModel.class).bindFromRequest(request);
    	KeywordModel keywordModel = searchForm.get();
    	String keyword = keywordModel.getKeyword();
    	
		return ws.url("https://api.github.com/search/repositories")
				.addQueryParameter("q", keyword)
                .get() // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
                .thenApplyAsync(result -> {
                    try {

                    	ObjectMapper objectMapper = new ObjectMapper();
                    	JsonNode rootNode = result.asJson();
                    	SearchModel searchResult = objectMapper.readValue(rootNode.toString(), SearchModel.class);
                    	List<Repositories> repos = searchResult.getItems();
                        return ok(index.render(formFactory.form(KeywordModel.class), repos));
                    }
                    catch(Exception e) {
                    	return ok(e.toString());
                    }
                }, httpExecutionContext.current());
    	
    }

}
