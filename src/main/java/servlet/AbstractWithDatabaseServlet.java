package servlet;

import db.ProductDatabase;

import javax.servlet.http.HttpServlet;

public abstract class AbstractWithDatabaseServlet extends HttpServlet {
    protected final ProductDatabase db;

    public AbstractWithDatabaseServlet(ProductDatabase db) {
        this.db = db;
    }
}
