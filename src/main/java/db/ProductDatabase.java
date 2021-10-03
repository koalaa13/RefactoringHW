package db;

import db.sql.SQLAttribute;
import db.sql.SQLQueryBuilder;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO remove copy-paste 'try(get connection)'
public class ProductDatabase {
    private static final String TABLE_NAME = "PRODUCT";
    private final String dbFile;
    private static final String SCHEMA = "jdbc:sqlite";
    private static final SQLAttribute[] ATTRIBUTES = new SQLAttribute[]{
            new SQLAttribute("NAME", SQLAttribute.SQLAttributeType.TEXT, false),
            new SQLAttribute("PRICE", SQLAttribute.SQLAttributeType.INT, false)};

    public ProductDatabase(String dbFile) {
        this.dbFile = dbFile;
    }

    private String getUrl() {
        return SCHEMA + ":" + dbFile;
    }

    public void createTable() throws SQLException {
        try (Connection c = DriverManager.getConnection(getUrl())) {
            String sql = SQLQueryBuilder.buildCreateTableSQLQuery(TABLE_NAME, ATTRIBUTES);
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    public void insertItemIntoTable(Product product) throws SQLException {
        try (Connection c = DriverManager.getConnection(getUrl())) {
            String sql = SQLQueryBuilder.buildInsertSQLQuery(TABLE_NAME, getAttributesFromEntity(product));
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    private SQLAttribute[] getAttributesFromEntity(Product product) {
        SQLAttribute[] attributes = new SQLAttribute[ATTRIBUTES.length];
        Field[] fields = product.getClass().getDeclaredFields();
        for (int i = 0; i < ATTRIBUTES.length; ++i) {
            // name of attr = field name in uppercase
            String attrName = fields[i].getName().toUpperCase();
            Field field = fields[i];
            // finding attr with needed name
            List<SQLAttribute> res = Arrays.stream(ATTRIBUTES)
                    .filter(attr -> attr.getName().equals(attrName))
                    .collect(Collectors.toList());
            // if we have no found attr or found more than 1 it's not ok
            if (res.size() != 1) {
                throw new RuntimeException("Can't get SQLAttribute for field " + field.getName());
            }
            SQLAttribute attrToAdd = res.get(0);
            try {
                attributes[i] = new SQLAttribute(
                        attrToAdd.getName(),
                        attrToAdd.getType(),
                        attrToAdd.isNullability(),
                        //magic to invoke getter by field name
                        new PropertyDescriptor(field.getName(), Product.class).getReadMethod().invoke(product).toString());
            } catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
                throw new RuntimeException("Can't invoke getter for field " + field.getName());
            }
        }
        return attributes;
    }
}
