import db.ProductDatabase;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.AddProductServlet;
import servlet.GetProductsServlet;
import servlet.QueryServlet;

public class App {
    public static void main(String[] args) throws Exception {
        ProductDatabase db = new ProductDatabase(args[0]);
        int port = Integer.parseInt(args[1]);
        db.create();

        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(db)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(db)), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(db)), "/query");

        server.start();
        server.join();
    }
}
