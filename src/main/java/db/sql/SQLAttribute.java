package db.sql;

public class SQLAttribute {
    private final String name;
    private final String type;
    private final boolean nullability;
    private final String value;

    public SQLAttribute(String name,
                        String type,
                        boolean nullability) {
        this.name = name;
        this.type = type;
        this.nullability = nullability;
        this.value = null;
    }

    public SQLAttribute(String name, String type, boolean nullability, String value) {
        this.name = name;
        this.type = type;
        this.nullability = nullability;
        this.value = value;
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

    public String getValue() {
        return value;
    }
}
