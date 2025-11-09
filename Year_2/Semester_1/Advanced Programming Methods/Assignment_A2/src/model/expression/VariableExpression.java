package model.expression;

import exceptions.VariableNotDefinedException;

import model.value.IValue;
import model.state.ISymbolTable;

public record VariableExpression(String variableName) implements IExpression
{
    @Override
    public IValue evaluate(ISymbolTable symbolTable){
        if(!symbolTable.isDefined(variableName)){
            throw new VariableNotDefinedException("Variable '" + variableName + "' not defined.");
        }
        return (IValue) symbolTable.getValue(variableName);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(variableName);
    }
}
