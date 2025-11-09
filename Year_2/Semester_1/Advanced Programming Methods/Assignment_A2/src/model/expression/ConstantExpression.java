package model.expression;

import model.state.ISymbolTable;
import model.value.IValue;

public record ConstantExpression(IValue value) implements IExpression {
    @Override
    public IValue evaluate(ISymbolTable symbolTable){
        return value;
    }

    @Override
    public IExpression deepCopy() {
        return new ConstantExpression(value); // valorile sunt imutabile, nu trebuie deep copy
    }
}
