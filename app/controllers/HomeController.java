package controllers;

import play.mvc.*;
import views.html.*;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Helper.CacheController;
import java.util.stream.*;
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
 * @author Sayali Kulkarni, Kirthana Senguttuvan
 */
public class HomeController extends Controller {

	private final WSClient ws;
	private HttpExecutionContext httpExecutionContext;
	FormFactory formFactory;
	private static CacheController cache = new CacheController();

	  public Result index() {
		  Map<String, List<Repositories>> cacheMap = new HashMap<>();
		  return ok(index.render(formFactory.form(KeywordModel.class), cacheMap)); 
	  }
	 
	@Inject
	HomeController(WSClient ws, HttpExecutionContext httpExecutionContext, FormFactory formFactory){
		this.ws = ws;
		this.httpExecutionContext = httpExecutionContext;
		this.formFactory = formFactory;
	}
    
    public Result getSearchResult(Http.Request request){
    	
    	Form<KeywordModel> searchForm = formFactory.form(KeywordModel.class).bindFromRequest(request);
    	KeywordModel keywordModel = searchForm.get();
    	String keyword = keywordModel.getKeyword();
    	return redirect(routes.HomeController.getSearch(keyword));
    	
	}

    public CompletionStage<Result> getSearch(String keyword){
    	return ws.url("https://api.github.com/search/repositories")
				.addQueryParameter("q", keyword)
                .get() // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
                .thenApplyAsync(result -> {
                    try {

                    	ObjectMapper objectMapper = new ObjectMapper();
                    	JsonNode rootNode = result.asJson();
              		  		
                    	SearchModel searchResult = objectMapper.readValue(rootNode.toString(), SearchModel.class);
                    	List<Repositories> repos = searchResult.getItems().stream().limit(10).collect(Collectors.toList());
                    	cache.setCache(keyword, repos);
                    	Map<String, List<Repositories>> cacheMap = cache.get_cache();
                        return ok(index.render(formFactory.form(KeywordModel.class), cacheMap));
                    }
                    catch(Exception e) {
                    	return ok(e.toString());
                    }
                }, httpExecutionContext.current());
    	

    }
}
