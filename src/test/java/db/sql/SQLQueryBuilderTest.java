package db.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLQueryBuilderTest {
    @Test
    public void buildCreateTableQueryTest() {
        String query = SQLQueryBuilder.buildCreateTableSQLQuery("PRODUCT",
                new SQLAttribute("NAME", "TEXT", false),
                new SQLAttribute("PRICE", "INT", false));

        String rightQuery = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "NAME TEXT NOT NULL, " +
                "PRICE INT NOT NULL)";
        assertEquals(rightQuery, query);
    }

}
