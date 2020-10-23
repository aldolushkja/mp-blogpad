package blogpad.reactor.health;

import blogpad.reactor.posts.control.PostsResourceClient;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Readiness
@Liveness
public class HealthProbe implements HealthCheck {

    @Inject
    @RestClient
    PostsResourceClient client;

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("content-availability").state(this.checkContentAvailability()).build();
    }

    boolean checkContentAvailability() {
        try {
            var response = this.client.findPost("initial");
            return response.getStatus() == 200;
        } catch (Exception ex) {
            return false;
        }
    }
}
