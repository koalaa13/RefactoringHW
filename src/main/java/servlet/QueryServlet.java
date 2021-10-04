package servlet;

import db.Product;
import db.ProductDatabase;
import http.ResponseManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class QueryServlet extends AbstractWithDatabaseServlet {
    public QueryServlet(ProductDatabase db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        ResponseManager manager = new ResponseManager(response);
        try {
            if ("max".equals(command)) {
                Product p = db.getMaxByPrice();
                manager.setHeader("Product with max price: ", 1);
                manager.addLine(p.getName() + "\t" + p.getPrice());
            } else if ("min".equals(command)) {
                Product p = db.getMinByPrice();
                manager.setHeader("Product with min price: ", 1);
                manager.addLine(p.getName() + "\t" + p.getPrice());
            } else if ("sum".equals(command)) {
                manager.setHeader("Summary price: ", 0);
                manager.addLine(String.valueOf(db.getPricesSum()));
            } else if ("count".equals(command)) {
                manager.setHeader("Number of products: ", 0);
                manager.addLine(String.valueOf(db.getCount()));
            } else {
                manager.setHeader("Unknown command: " + command, 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        manager.sendResponse();
    }
}
