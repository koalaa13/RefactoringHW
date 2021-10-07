package db.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SQLQueryBuilderTest {
    @Test
    public void buildCreateTableQueryTest() {
        String query = SQLQueryBuilder.buildCreateTableSQLQuery("PRODUCT",
                new SQLAttribute("NAME", SQLAttribute.SQLAttributeType.TEXT, false),
                new SQLAttribute("PRICE", SQLAttribute.SQLAttributeType.INT, false));

        String rightQuery = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "NAME TEXT NOT NULL, " +
                "PRICE INT NOT NULL)";
        assertEquals(rightQuery, query);
    }

    @Test
    public void buildInsertQueryTest() {
        final String name = "phone";
        final String price = "777";
        String rightQuery = "INSERT INTO PRODUCT " +
                "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
        String query = SQLQueryBuilder.buildInsertSQLQuery("PRODUCT",
                new SQLAttribute("NAME", SQLAttribute.SQLAttributeType.TEXT, false, name),
                new SQLAttribute("PRICE", SQLAttribute.SQLAttributeType.INT, false, price));
        assertEquals(rightQuery, query);
    }

    @Test
    public void buildSelectAllSQLQueryTest() {
        String rightQuery = "SELECT * FROM PRODUCT";
        String query = SQLQueryBuilder.buildSelectAllSQLQuery("PRODUCT");
        assertEquals(rightQuery, query);
    }

    @Test
    public void buildSelectAllOrderBySQLQueryTest() {
        String rightQuery = "SELECT * FROM PRODUCT ORDER BY PRICE";
        String query = SQLQueryBuilder.buildSelectAllOrderBySQLQuery("PRODUCT", "PRICE", false, null);
        assertEquals(rightQuery, query);
    }


    @Test
    public void buildSelectAllOrderByDescSQLQueryTest() {
        String rightQuery = "SELECT * FROM PRODUCT ORDER BY PRICE DESC";
        String query = SQLQueryBuilder.buildSelectAllOrderBySQLQuery("PRODUCT", "PRICE", true, null);
        assertEquals(rightQuery, query);
    }

    @Test
    public void buildSelectAllOrderByWithLimitSQLQueryTest() {
        String rightQuery = "SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 10";
        String query = SQLQueryBuilder.buildSelectAllOrderBySQLQuery("PRODUCT", "PRICE", false, 10);
        assertEquals(rightQuery, query);
    }

    @Test
    public void buildAllSumBySQLQueryTest() {
        String rightQuery = "SELECT SUM(PRICE) FROM PRODUCT";
        String query = SQLQueryBuilder.buildAllSumBySQLQuery("PRODUCT", "PRICE");
        assertEquals(rightQuery, query);
    }

    @Test
    public void buildAllCountSQLQueryTest() {
        String rightQuery = "SELECT COUNT(*) FROM PRODUCT";
        String query = SQLQueryBuilder.buildAllCountSQLQuery("PRODUCT");
        assertEquals(rightQuery, query);
    }

    @Test
    void buildDropTableSQLQuery() {
        String rightQuery = "DELETE FROM PRODUCT";
        String query = SQLQueryBuilder.buildClearTableSQLQuery("PRODUCT");
        assertEquals(rightQuery, query);
    }
}
