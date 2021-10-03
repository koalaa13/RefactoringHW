package db.sql;

public class SQLAttribute {
    public enum SQLAttributeType {
        TEXT(true), INT(false);

        private final boolean text;

        SQLAttributeType(boolean text) {
            this.text = text;
        }

        public boolean isText() {
            return text;
        }
    }

    private final String name;
    private final SQLAttributeType type;
    private final boolean nullability;
    private final String value;

    public SQLAttribute(String name,
                        SQLAttributeType type,
                        boolean nullability) {
        this.name = name;
        this.type = type;
        this.nullability = nullability;
        this.value = null;
    }

    public SQLAttribute(String name,
                        SQLAttributeType type,
                        boolean nullability,
                        String value) {
        this.name = name;
        this.type = type;
        this.nullability = nullability;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public SQLAttributeType getType() {
        return type;
    }

    public boolean isNullability() {
        return nullability;
    }

    public String getValue() {
        return value;
    }
}
