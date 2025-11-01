package model.value;

import model.type.Type;

public record BooleanValue(boolean value) implements IValue {
    @Override
    public Type getType(){
        return Type.BOOLEAN;
    }
}
