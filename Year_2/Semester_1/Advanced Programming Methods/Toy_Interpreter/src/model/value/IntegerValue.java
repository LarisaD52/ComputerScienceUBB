package model.value;

import model.type.IType;
import model.type.IntegerType;

public record IntegerValue(int value) implements IValue {

    @Override
    public IType getType() {
        return new IntegerType();
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof IntegerValue other)) return false;
        return this.value == other.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
