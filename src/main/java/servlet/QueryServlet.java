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
                if (p == null) {
                    responseBodyManager.setHeader("There is no products.", 1);
                } else {
                    responseBodyManager.setHeader("Product with max price: ", 1);
                    responseBodyManager.addLine(p.toString());
                }
            } else if ("min".equals(command)) {
                Product p = db.getMinByPrice();
                if (p == null) {
                    responseBodyManager.setHeader("There is no products.", 1);
                } else {
                    responseBodyManager.setHeader("Product with min price: ", 1);
                    responseBodyManager.addLine(p.toString());
                }
            } else if ("sum".equals(command)) {
                responseBodyManager.setHeader("Summary price: " + db.getPricesSum(), 0);
            } else if ("count".equals(command)) {
                responseBodyManager.setHeader("Number of products: " + db.getCount(), 0);
            } else if (command == null) {
                responseBodyManager.setHeader("Should type a command.", 1);
            } else {
                responseBodyManager.setHeader("Unknown command: " + command, 1);
            }
            sendResponse(response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
