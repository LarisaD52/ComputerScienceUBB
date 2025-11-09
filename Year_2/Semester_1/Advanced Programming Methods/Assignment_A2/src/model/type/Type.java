package model.type;

import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntegerValue;
import model.value.StringValue;

public enum Type {
    INTEGER,
    BOOLEAN,
    STRING;

    public IValue getDefaultValue() {
        switch (this) {
            case INTEGER:
                return new IntegerValue(0);

            case BOOLEAN:
                return new BooleanValue(false);

            case STRING:
                return new StringValue("");
        }
        return null;
    }

    @Override
    public String toString() {
        return switch (this) {
            case INTEGER -> "int";
            case BOOLEAN -> "bool";
            case STRING -> "string";
        };
    }
}
