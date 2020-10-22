package blogpad.reactor.posts.control;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RendererTest {

    private Renderer cut;

    @BeforeEach
    public void init() {
        this.cut = new Renderer();
    }

    @Test
    public void render() {
        var actual = this.cut.render("template", "input");
        System.out.println(" -- " + actual);
        Assertions.assertEquals("duke42", actual);
    }

}