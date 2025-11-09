package model.value;

import model.type.Type;

public record StringValue(String value) implements IValue {
    public Type getType(){
        return Type.STRING;
    }

    public String getVal() {
        return value;
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (!(another instanceof StringValue)) return false;
        StringValue other = (StringValue) another;
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return value;
    }

}
