package servlet;

import db.Product;
import db.ProductDatabase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GetProductsServlet extends AbstractWithDatabaseServlet {
    public GetProductsServlet(ProductDatabase db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().println("<html><body>");
            List<Product> products = db.findAll();
            for (Product p : products) {
                response.getWriter().println(p.getName() + "\t" + p.getPrice() + "</br>");
            }
            response.getWriter().println("</html></body>");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
