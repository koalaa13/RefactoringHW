package db.sql;

public class SQLQueryBuilder {
    public static String getCreateTableSQLQuery(String tableName, SQLAttribute... attributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName);
        sb.append("(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,");
        for (int i = 0; i < attributes.length; ++i) {
            SQLAttribute attribute = attributes[i];
            sb.append(' ').append(attribute.getName())
                    .append(' ').append(attribute.getType())
                    .append(attribute.isNullability() ? "NOT NULL" : "NULL");
            if (i + 1 == attributes.length) {
                sb.append(')');
            } else {
                sb.append(',');
            }
        }
        return sb.toString();
    }
}
