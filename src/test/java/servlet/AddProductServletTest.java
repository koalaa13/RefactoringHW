package servlet;

import db.ProductDatabase;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AddProductServletTest {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final int PORT = 8082;
    private static final Server SERVER = new Server(PORT);
    private static final String RESPONSE_BODY_FOR_CORRECT_REQUEST = "<html><body>\nOK\n</body></html>\n";

    @BeforeAll
    public static void beforeAll() throws Exception {
        //created database
        File file = File.createTempFile("database", "testing", new File("."));
        file.deleteOnExit();
        ProductDatabase db = new ProductDatabase(file.getAbsolutePath());
        db.create();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        SERVER.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(db)), "/add-product");
        SERVER.start();
    }

    @Test
    public void correctQueryTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + "/add-product?name=phone&price=333"))
                .header("accept", "text/html")
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(RESPONSE_BODY_FOR_CORRECT_REQUEST, response.body());
    }

    @Test
    public void noNameRequestTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + "/add-product?price=333"))
                .header("accept", "text/html")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        // here response.body is a stacktrace actually,
        // but I won't parse it
        assertNotEquals(RESPONSE_BODY_FOR_CORRECT_REQUEST, response.body());
    }

    @Test
    public void noPriceRequestTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + "/add-product?name=phone"))
                .header("accept", "text/html")
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        // here response.body is a stacktrace actually,
        // but I won't parse it
        assertNotEquals(RESPONSE_BODY_FOR_CORRECT_REQUEST, response.body());
    }

    @AfterAll
    public static void afterAll() throws Exception {
        SERVER.stop();
    }
}
