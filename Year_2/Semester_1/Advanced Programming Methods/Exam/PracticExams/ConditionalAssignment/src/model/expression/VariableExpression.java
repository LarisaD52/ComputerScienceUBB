package model.expression;

import exceptions.MyException;
import exceptions.VariableNotDefinedException;

import model.state.IHeap;
import model.type.IType;
import model.value.IValue;
import model.state.ISymbolTable;


public record VariableExpression(String variableName) implements IExpression
{
    @Override
    public IValue evaluate(ISymbolTable symbolTable, IHeap heap){
        if(!symbolTable.isDefined(variableName)){
            throw new VariableNotDefinedException("Variable '" + variableName + "' not defined.");
        }
        return (IValue) symbolTable.getValue(variableName);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(variableName);
    }


    @Override
    public IType typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        // typeEnv.lookup(id) este echivalent cu typeEnv.getValue(variableName) in codu meu
        if (!typeEnv.isDefined(variableName)) {
            throw new MyException("Variable " + variableName + " is not defined in the type environment.");
        }
        return typeEnv.getValue(variableName);
    }


}
