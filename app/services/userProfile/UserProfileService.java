package services.userProfile;

import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.UserProfileModel;
import services.userProfile.*;

import play.libs.ws.WSResponse;
public class UserProfileService {
	
	@Inject
	private UserProfileApi userProfile;
	
	private ObjectMapper mapper;
	
	public UserProfileService(){
		mapper = new ObjectMapper();
	}
	
	public CompletionStage<UserProfileModel> getUserProfileService(String keyword){
		return userProfile.getUserProfile(keyword)
				.thenApplyAsync(WSResponse::asJson)
	            .thenApplyAsync(this::parseObject);
	}
	
	public UserProfileModel parseObject(JsonNode result) {
		
		try {
			return mapper.readValue(result.toString(), UserProfileModel.class);
        } catch (JsonProcessingException e) {
            return null;
        }
	}
}
