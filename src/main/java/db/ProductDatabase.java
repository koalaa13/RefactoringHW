package db;

import db.sql.SQLAttribute;
import db.sql.SQLQueryBuilder;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public void create() throws SQLException {
        invokeStatementExecuteUpdate(SQLQueryBuilder.buildCreateTableSQLQuery(TABLE_NAME, ATTRIBUTES));
    }

    public void save(Product product) throws SQLException {
        invokeStatementExecuteUpdate(SQLQueryBuilder.buildInsertSQLQuery(TABLE_NAME, getAttributesFromEntity(product)));
    }

    public List<Product> findAll() throws SQLException {
        return invokeStatementExecuteQuery(SQLQueryBuilder.buildSelectALlSQLQuery(TABLE_NAME));
    }

    @FunctionalInterface
    private interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    private void invokeWithStatement(SQLConsumer<Statement> consumer) throws SQLException {
        try (Connection c = DriverManager.getConnection(getUrl())) {
            Statement stmt = c.createStatement();
            consumer.accept(stmt);
            stmt.close();
        }
    }

    private List<Product> invokeStatementExecuteQuery(String query) throws SQLException {
        List<Product> res = new ArrayList<>();
        invokeWithStatement(stmt -> {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                res.add(getEntityFromResultSet(rs));
            }
            rs.close();
        });
        return res;
    }

    private void invokeStatementExecuteUpdate(String query) throws SQLException {
        invokeWithStatement(stmt -> stmt.executeUpdate(query));
    }

    private Product getEntityFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        for (SQLAttribute attr : ATTRIBUTES) {
            Method setter;
            try {
                setter = new PropertyDescriptor(attr.getName().toLowerCase(), Product.class).getWriteMethod();
            } catch (IntrospectionException e) {
                throw new RuntimeException("Can't get setter for field " + attr.getName().toLowerCase(), e);
            }
            Object value;
            switch (attr.getType()) {
                case INT:
                    value = rs.getInt(attr.getName());
                    break;
                case TEXT:
                    value = rs.getString(attr.getName());
                    break;
                default:
                    throw new RuntimeException("Can't get type of attribute");
            }
            try {
                setter.invoke(product, value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Can't invoke setter for field " + attr.getName().toLowerCase());
            }

        }
        return product;
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
