package model.value;

import model.type.Type;

public record BooleanValue(boolean value) implements IValue {

    public Type getType(){
        return Type.BOOLEAN;
    }


    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof BooleanValue)) return false;
        BooleanValue other = (BooleanValue) another;
        return this.value == other.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
