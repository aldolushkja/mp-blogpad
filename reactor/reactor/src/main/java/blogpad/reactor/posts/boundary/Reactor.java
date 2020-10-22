package blogpad.reactor.posts.boundary;

import blogpad.reactor.posts.control.PostsResourceClient;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.graalvm.polyglot.Context;

import javax.inject.Inject;
import javax.json.JsonValue;

public class Reactor {

    @Inject
    @RestClient
    PostsResourceClient client;

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    MetricRegistry registry;

    public String render(String title) {
        var response = this.client.findPost(title);
        var status = response.getStatus();
        registry.counter("content_find_post_status_" + status).inc();
        return "rendered" + response.readEntity(JsonValue.class);
    }

    String render(String template, String input) {
        try (Context context = Context.create("js")) {
            var bindings = context.getBindings("js");
            bindings.putMember("message", "duke");
            return context.eval("js", "message +  42").asString();
        }
    }

}
