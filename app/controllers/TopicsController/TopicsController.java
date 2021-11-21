package controllers.TopicsController;

import java.util.concurrent.CompletionStage;

import model.KeywordModel;
import model.SearchModel;
import play.data.Form;
import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import views.html.UserProfileView.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import play.libs.concurrent.HttpExecutionContext;
import javax.inject.Inject;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import model.UserProfileModel;
import model.Repositories;
import views.html.TopicsView.TopicsView;

/**
 * This controller contains an action to handle HTTP requests
 * to the user profile page.
 */
public class TopicsController extends Controller {

    private final WSClient ws;
    List<Repositories> repos = new ArrayList<Repositories>();

    private HttpExecutionContext httpExecutionContext;

    @Inject
    TopicsController(WSClient ws, HttpExecutionContext httpExecutionContext){
        this.ws = ws;
        this.httpExecutionContext = httpExecutionContext;
    }
    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */

    public CompletionStage<Result> getSearchResult(Http.Request request, String keyword){


        return ws.url("https://api.github.com/search/repositories?q=topic:"+keyword)
                .get() // THIS IS NOT BLOCKING! It returns a promise to the response. It comes from WSRequest.
                .thenApplyAsync(result -> {
                    try {

                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = result.asJson();
                        SearchModel searchResult = objectMapper.readValue(rootNode.toString(), SearchModel.class);
                        List<Repositories> repos = searchResult.getItems().stream().limit(10).collect(Collectors.toList());
                        return ok(TopicsView.render(repos));
                    }
                    catch(Exception e) {
                        return ok(e.toString());
                    }
                }, httpExecutionContext.current());

    }
}
