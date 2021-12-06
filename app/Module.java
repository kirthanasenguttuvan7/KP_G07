
import com.google.inject.AbstractModule;

import actors.UserActor;
import actors.UserParentActor;
import play.libs.akka.AkkaGuiceSupport;
import services.GithubApi;
import services.HomeControllerImplementation;
import services.userProfile.UserProfileApi;
import services.userProfile.UserProfileImplementation;

@SuppressWarnings("unused")
public class Module extends AbstractModule implements AkkaGuiceSupport {
	
	@Override
    public void configure() {
		bindActor(UserParentActor.class, "userParentActor");
        bindActorFactory(UserActor.class, UserActor.Factory.class);
        bind(GithubApi.class).to(HomeControllerImplementation.class);
        bind(UserProfileApi.class).to(UserProfileImplementation.class);
    }
}
