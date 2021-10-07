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

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryServletTest {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final int PORT = 8082;
    private static final Server SERVER = new Server(PORT);
    private static ProductDatabase db;
    private static final String SERVLET_PATH = "/query";

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

        context.addServlet(new ServletHolder(new QueryServlet(db)), SERVLET_PATH);
        SERVER.start();
    }

    @Test
    public void emptyDbTest() throws IOException, InterruptedException {
        final HttpRequest requestMin = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=min"))
                .header("accept", "text/html")
                .build();
        HttpResponse<String> response = CLIENT.send(requestMin, HttpResponse.BodyHandlers.ofString());
        assertEquals("<html><body>\n<h1>There is no products.</h1>\n</body></html>\n", response.body());

        final HttpRequest requestMax = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=max"))
                .header("accept", "text/html")
                .build();
        response = CLIENT.send(requestMax, HttpResponse.BodyHandlers.ofString());
        assertEquals("<html><body>\n<h1>There is no products.</h1>\n</body></html>\n", response.body());

        final HttpRequest requestCount = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=count"))
                .header("accept", "text/html")
                .build();
        response = CLIENT.send(requestCount, HttpResponse.BodyHandlers.ofString());
        assertEquals("<html><body>\nNumber of products: 0\n</body></html>\n", response.body());

        final HttpRequest requestSum = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=sum"))
                .header("accept", "text/html")
                .build();
        response = CLIENT.send(requestSum, HttpResponse.BodyHandlers.ofString());
        assertEquals("<html><body>\nSummary price: 0\n</body></html>\n", response.body());
    }

    @Test
    public void noAttribute() throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + SERVLET_PATH))
                .header("accept", "text/html")
                .build();
        assertEquals("<html><body>\n<h1>Should type a command.</h1>\n</body></html>\n",
                CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body());
    }

    @Test
    public void wrongCommandTest() throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=kek"))
                .header("accept", "text/html")
                .build();
        assertEquals("<html><body>\n<h1>Unknown command: kek</h1>\n</body></html>\n",
                CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body());
    }

    @Test
    public void commandMaxTest() throws SQLException, IOException, InterruptedException {
        Product product = new Product("phone", 777);
        db.save(product);

        final HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=max"))
                .header("accept", "text/html")
                .build();
        assertEquals("<html><body>\n<h1>Product with max price: </h1>\n" + product + "<br>\n</body></html>\n",
                CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body());
    }

    @Test
    public void commandMinTest() throws SQLException, IOException, InterruptedException {
        Product product = new Product("phone", 777);
        db.save(product);

        final HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=min"))
                .header("accept", "text/html")
                .build();
        assertEquals("<html><body>\n<h1>Product with min price: </h1>\n" + product + "<br>\n</body></html>\n",
                CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body());
    }

    @Test
    public void commandSumTest() throws SQLException, IOException, InterruptedException {
        Product product = new Product("phone", 777);
        db.save(product);

        final HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=sum"))
                .header("accept", "text/html")
                .build();
        assertEquals("<html><body>\nSummary price: " + product.getPrice() + "\n</body></html>\n",
                CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body());
    }


    @Test
    public void commandCountTest() throws SQLException, IOException, InterruptedException {
        Product product = new Product("phone", 777);
        db.save(product);

        final HttpRequest request = HttpRequest.newBuilder(
                        URI.create("http://localhost:" + PORT + SERVLET_PATH + "?command=count"))
                .header("accept", "text/html")
                .build();
        assertEquals("<html><body>\nNumber of products: 1\n</body></html>\n",
                CLIENT.send(request, HttpResponse.BodyHandlers.ofString()).body());
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
