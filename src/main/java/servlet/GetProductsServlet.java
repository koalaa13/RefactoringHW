package servlet;

import db.Product;
import db.ProductDatabase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GetProductsServlet extends AbstractServlet {
    public GetProductsServlet(ProductDatabase db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Product> products = db.findAll();
            for (Product p : products) {
                responseBodyManager.addLine(p.toString());
            }
            sendResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
