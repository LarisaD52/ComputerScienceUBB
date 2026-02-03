package model.expression;

import exceptions.MyException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.IType;
import model.value.IValue;
import model.value.StringValue;

import java.lang.reflect.Type;

public record ConstantExpression(IValue value) implements IExpression {
    @Override
    public IValue evaluate(ISymbolTable symbolTable, IHeap heap){
        return value;
    }

    @Override
    public IExpression deepCopy() {
        return new ConstantExpression(value); // valorile sunt imutabile, nu trebuie .deepCopy
    }

    @Override
    public IType typecheck(ISymbolTable<String, IType> typeEnv) throws MyException{
        return value.getType();
    }
}
