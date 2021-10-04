package db.sql;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SQLQueryBuilder {
    public static String buildCreateTableSQLQuery(String tableName, SQLAttribute... attributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName);
        sb.append("(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
        String attrs = Arrays.stream(attributes)
                .map(a -> a.getName() + ' ' + a.getType() + ' ' + (a.isNullability() ? "NULL" : "NOT NULL"))
                .collect(Collectors.joining(", "));
        sb.append(attrs).append(')');
        return sb.toString();
    }

    public static String buildInsertSQLQuery(String tableName, SQLAttribute... attributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableName).append(' ');
        sb.append('(');
        sb.append(Arrays.stream(attributes).map(SQLAttribute::getName).collect(Collectors.joining(", ")));
        sb.append(") VALUES (");
        sb.append(Arrays.stream(attributes).map(a -> {
            if (a.getType().isText()) {
                return "\"" + a.getValue() + "\"";
            }
            return a.getValue();
        }).collect(Collectors.joining(",")));
        sb.append(')');
        return sb.toString();
    }

    public static String buildSelectAllSQLQuery(String tableName) {
        return "SELECT * FROM " + tableName;
    }

    public static String buildSelectAllOrderBySQLQuery(String tableName,
                                                       String attrName,
                                                       boolean desc,
                                                       Integer limit) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ")
                .append(tableName)
                .append(" ORDER BY ")
                .append(attrName).append(' ');
        if (desc) {
            sb.append("DESC ");
        }
        if (limit != null) {
            sb.append("LIMIT ").append(limit);
        }
        return sb.toString();
    }
}
