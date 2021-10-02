package db;

import db.sql.SQLAttribute;
import db.sql.SQLQueryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final String dbFile;
    private static final String SCHEMA = "jdbc:sqlite";

    public Database(String dbFile) {
        this.dbFile = dbFile;
    }

    private String getUrl() {
        return SCHEMA + ":" + dbFile;
    }

    public void createProductsTable() throws SQLException {
        try (Connection c = DriverManager.getConnection(getUrl())) {
            String sql = SQLQueryBuilder.buildCreateTableSQLQuery("PRODUCT",
                    new SQLAttribute("NAME", "TEXT", false),
                    new SQLAttribute("PRICE", "INT", false));
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }
}
