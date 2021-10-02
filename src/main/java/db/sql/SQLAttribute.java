package db.sql;

public class SQLAttribute {
    private final String name;
    private final String type;
    private final boolean nullability;

    public SQLAttribute(String name, String type, boolean nullability) {
        this.name = name;
        this.type = type;
        this.nullability = nullability;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isNullability() {
        return nullability;
    }
}
