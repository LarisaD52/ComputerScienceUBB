package model.value;

import model.type.IType;
import model.type.BooleanType;

public record BooleanValue(boolean value) implements IValue {

    @Override
    public IType getType() {
        return new BooleanType();
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof BooleanValue other)) return false;
        return this.value == other.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
