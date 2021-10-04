package servlet;

import db.Product;
import db.ProductDatabase;
import http.ResponseManager;

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
            ResponseManager manager = new ResponseManager(response);
            List<Product> products = db.findAll();
            for (Product p : products) {
                manager.addLine(p.getName() + "\t" + p.getPrice());
            }
            manager.sendResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
