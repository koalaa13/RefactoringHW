package servlet;

import db.Product;
import db.ProductDatabase;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetProductsServletTest {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final int PORT = 8082;
    private static final Server SERVER = new Server(PORT);
    private static final List<Product> PRODUCTS = List.of(
            new Product("phone", 777),
            new Product("computer", 10000),
            new Product("apple", 30),
            new Product("Dmozze", 1),
            new Product("oil", -100)
    );
    private static ProductDatabase db;
    private static final String SERVLET_PATH = "/get-products";
    private static final HttpRequest request = HttpRequest.newBuilder(
                    URI.create("http://localhost:" + PORT + SERVLET_PATH))
            .header("accept", "text/html")
            .build();

    @BeforeAll
    public static void beforeAll() throws Exception {
        //created database
        File file = File.createTempFile("database", "testing", new File("."));
        file.deleteOnExit();
        db = new ProductDatabase(file.getAbsolutePath());
        db.create();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        SERVER.setHandler(context);

        context.addServlet(new ServletHolder(new GetProductsServlet(db)), SERVLET_PATH);
        SERVER.start();
    }

    @Test
    public void notEmptyDbTest() throws SQLException, IOException, InterruptedException {
        for (Product p : PRODUCTS) {
            db.save(p);
        }
        String rightResponse = PRODUCTS.stream()
                .map(Product::toString)
                .collect(Collectors.joining("<br>\n"));
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("<html><body>\n" + rightResponse + "<br>\n</body></html>\n", response.body());
    }

    @Test
    public void emptyDbTest() throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals("<html><body>\n</body></html>\n", response.body());
    }

    @AfterEach
    public void afterEach() throws SQLException {
        db.clearData();
    }

    @AfterAll
    public static void afterAll() throws Exception {
        SERVER.stop();
    }
}
