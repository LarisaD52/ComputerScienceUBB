package model.type;

import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntegerValue;

public enum Type {
    INTEGER,
    BOOLEAN;

    public IValue getDefaultValue() {
        switch (this) {
            case INTEGER:
                return new IntegerValue(0);

            case BOOLEAN:
                return new BooleanValue(false);
        }
        return null;
    }

    @Override
    public String toString() {
        return switch (this) {
            case INTEGER -> "int";
            case BOOLEAN -> "bool";
        };
    }
}
