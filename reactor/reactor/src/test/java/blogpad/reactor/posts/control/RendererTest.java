package blogpad.reactor.posts.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RendererTest {

    private Renderer cut;

    @BeforeEach
    public void init() {
        this.cut = new Renderer();
    }

    @Test
    public void render() {
        var actual = this.cut.render("template-", "post");
        System.out.println(" -- " + actual);
        assertEquals("template-post12", actual);
    }

}