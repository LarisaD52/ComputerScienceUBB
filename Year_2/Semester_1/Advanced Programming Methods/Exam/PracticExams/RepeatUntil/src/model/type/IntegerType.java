package model.type;

import model.value.IValue;
import model.value.IntegerValue;

public class IntegerType implements IType {

    @Override
    public boolean equals(Object another) {
        return another instanceof IntegerType;
    }

    @Override
    public IValue getDefaultValue() {
        return new IntegerValue(0);
    }

    @Override
    public String toString() {
        return "int";
    }
}
