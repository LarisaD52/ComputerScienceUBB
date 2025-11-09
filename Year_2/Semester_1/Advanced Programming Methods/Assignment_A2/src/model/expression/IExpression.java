package model.expression;

import exceptions.MyException;
import model.state.ISymbolTable;
import model.value.IValue;

public interface IExpression {
    IValue evaluate(ISymbolTable<String,IValue> symbolTable) throws MyException;

    IExpression deepCopy();

}
