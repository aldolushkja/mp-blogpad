package blogpad.reactor.posts.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RendererTest {

    private Renderer cut;

    @BeforeEach
    public void init() {
        this.cut = new Renderer();
        this.cut.init();
    }

    @Test
    public void render() {
        var actual = this.cut.render("""
                <html>
                    <head>
                        <title>{{title}}</title>
                    </head>
                    <body>
                        <h1>{{title}}</h1>
                        <article>{{content}}<article>
                    </body>
                </html>
                """, """
                    {
                        "title":"First post",
                        "content":"Hello from unit test"
                    }
                """);
        System.out.println(" -- " + actual);
//        assertEquals("template-post12", actual);
    }

}