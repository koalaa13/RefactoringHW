package servlet;

import db.Product;
import db.ProductDatabase;
import http.ResponseManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProductServlet extends AbstractWithDatabaseServlet {
    public AddProductServlet(ProductDatabase db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = request.getParameter("name");
            int price = Integer.parseInt(request.getParameter("price"));
            db.save(new Product(name, price));
            ResponseManager responseManager = new ResponseManager(response);
            responseManager.setHeader("OK", 0);
            responseManager.sendResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
