package servlet;

import db.Product;
import db.ProductDatabase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProductServlet extends AbstractServlet {
    public AddProductServlet(ProductDatabase db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = request.getParameter("name");
            int price = Integer.parseInt(request.getParameter("price"));
            db.save(new Product(name, price));

            responseBodyManager.setHeader("OK", 0);
            sendResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
