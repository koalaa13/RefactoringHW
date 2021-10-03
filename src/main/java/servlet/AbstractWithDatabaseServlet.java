package servlet;

import db.ProductDatabase;

import javax.servlet.http.HttpServlet;

public abstract class AbstractWithDatabaseServlet extends HttpServlet {
    protected final ProductDatabase db;

    protected AbstractWithDatabaseServlet(ProductDatabase db) {
        this.db = db;
    }
}
