package servlet;

import db.Product;
import db.ProductDatabase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class QueryServlet extends AbstractServlet {
    public QueryServlet(ProductDatabase db) {
        super(db);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        try {
            if ("max".equals(command)) {
                Product p = db.getMaxByPrice();
                responseBodyManager.setHeader("Product with max price: ", 1);
                responseBodyManager.addLine(p.getName() + "\t" + p.getPrice());
            } else if ("min".equals(command)) {
                Product p = db.getMinByPrice();
                responseBodyManager.setHeader("Product with min price: ", 1);
                responseBodyManager.addLine(p.getName() + "\t" + p.getPrice());
            } else if ("sum".equals(command)) {
                responseBodyManager.setHeader("Summary price: ", 0);
                responseBodyManager.addLine(String.valueOf(db.getPricesSum()));
            } else if ("count".equals(command)) {
                responseBodyManager.setHeader("Number of products: ", 0);
                responseBodyManager.addLine(String.valueOf(db.getCount()));
            } else {
                responseBodyManager.setHeader("Unknown command: " + command, 1);
            }
            sendResponse(response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
