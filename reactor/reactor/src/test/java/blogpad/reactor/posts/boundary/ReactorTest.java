package blogpad.reactor.posts.boundary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReactorTest {

    private Reactor cut;

    @BeforeEach
    public void init() {
        this.cut = new Reactor();
    }

    @Test
    public void render() {
        var actual = this.cut.render("template", "input");
        System.out.println(" -- " + actual);
        Assertions.assertEquals("duke42", actual);
    }

}