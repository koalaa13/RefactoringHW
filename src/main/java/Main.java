import db.ProductDatabase;
import db.sql.SQLAttribute;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.AddProductServlet;
import servlet.GetProductsServlet;
import servlet.QueryServlet;

public class Main {
    private static final String dbFile = "test.db";
    private static final ProductDatabase db = new ProductDatabase(dbFile);
    private static final int PORT = 8081;

    public static void main(String[] args) throws Exception {
        db.createTable();

        Server server = new Server(PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(db)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet()), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet()), "/query");

        server.start();
        server.join();
    }
}
