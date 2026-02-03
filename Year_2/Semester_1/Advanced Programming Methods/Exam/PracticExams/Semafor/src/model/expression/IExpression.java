package model.expression;

import exceptions.MyException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.IType;
import model.value.IValue;



public interface IExpression {
    IValue evaluate(ISymbolTable<String,IValue> symbolTable, IHeap heap) throws MyException;

    IExpression deepCopy();

    IType typecheck(ISymbolTable<String, IType> typeEnv) throws MyException;
}

