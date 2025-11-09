package model.value;

import model.type.Type;

public record IntegerValue(int value) implements IValue{

    @Override
    public Type getType() {
        return Type.INTEGER;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof IntegerValue)) return false;
        IntegerValue other = (IntegerValue) another;
        return this.value == other.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
