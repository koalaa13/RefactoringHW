package html;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseBodyManagerTest {
    private ResponseBodyManager manager;
    private static final String EMPTY_BODY = "<html><body>\n</body></html>\n";

    @BeforeEach
    public void beforeEach() {
        manager = new ResponseBodyManager();
    }

    @Test
    public void emptyBodyTest() {
        assertEquals(EMPTY_BODY, manager.getResponseBody());
    }

    @Test
    public void headerTest() {
        final String header = "CHECK";
        manager.setHeader(header, 1);
        String rightBody = "<html><body>\n<h1>" + header + "</h1>\n</body></html>\n";
        assertEquals(rightBody, manager.getResponseBodyAndFlush());
        assertEquals(EMPTY_BODY, manager.getResponseBody());

        manager.setHeader(header, 0);
        rightBody = "<html><body>\n" + header + "\n</body></html>\n";
        assertEquals(rightBody, manager.getResponseBodyAndFlush());
        assertEquals(EMPTY_BODY, manager.getResponseBody());
    }

    @Test
    public void withoutFlushingTest() {
        final String header = "CHECK";
        manager.setHeader(header, 1);
        String rightBody = "<html><body>\n<h1>" + header + "</h1>\n</body></html>\n";
        assertEquals(rightBody, manager.getResponseBody());
        assertEquals(rightBody, manager.getResponseBodyAndFlush());
        assertEquals(EMPTY_BODY, manager.getResponseBody());
    }

    @Test
    public void withLinesTest() {
        manager.addLine("kek");
        manager.addLine("lol");
        String rightBody = "<html><body>\nkek<br>\nlol<br>\n</body></html>\n";
        assertEquals(rightBody, manager.getResponseBodyAndFlush());
        assertEquals(EMPTY_BODY, manager.getResponseBody());
    }
}
