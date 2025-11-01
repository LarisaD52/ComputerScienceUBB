package model.value;

import model.type.Type;

public record IntegerValue(int value) implements IValue{

    @Override
    public Type getType() {
        return Type.INTEGER;
    }
}
