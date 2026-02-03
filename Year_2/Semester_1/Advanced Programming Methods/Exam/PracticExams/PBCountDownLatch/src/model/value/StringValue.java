package model.value;

import model.type.IType;
import model.type.StringType;

public record StringValue(String value) implements IValue {

    @Override
    public IType getType() {
        return new StringType();
    }

    public String getVal() {
        return value;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof StringValue other)) return false;
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
