package blogpad.reactor.posts.boundary;

import blogpad.reactor.posts.control.PostsResourceClient;
import blogpad.reactor.posts.control.Renderer;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;

public class Reactor {

    @Inject
    @RestClient
    PostsResourceClient client;

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    MetricRegistry registry;

    @Inject
    Renderer renderer;

    public String render(String title) {
        var response = this.client.findPost(title);
        var status = response.getStatus();
        registry.counter("content_find_post_status_" + status).inc();
        var postAsString = response.readEntity(String.class);
        return this.renderer.render("""
                <html>
                    <head>
                        <title>{{title}}</title>
                    </head>
                    <body>
                        <h1>{{title}}</h1>
                        <article>{{content}}<article>
                    </body>
                </html>
                """, postAsString);
    }

}
