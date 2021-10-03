package db.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}
