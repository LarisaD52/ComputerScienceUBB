package model.statement;

import exceptions.InterpreterException;
import model.state.ILockTable;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.value.IValue;
import model.value.IntegerValue;

public class LockStatement implements IStatement {

    private final String var;

    public LockStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws InterpreterException {

        ISymbolTable<String, IValue> symTable = state.getSymbolTable();
        ILockTable lockTable = state.getLockTable();

        if (!symTable.isDefined(var))
            throw new InterpreterException("Variable not defined!");
        if (!symTable.getValue(var).getType().equals(new IntegerType()))
            throw new InterpreterException("Variable is not of type int!");
        int foundIndex = ((IntegerValue) symTable.getValue(var)).value();
        if (!lockTable.containsKey(foundIndex))
            throw new InterpreterException("Index not found in LockTable!");
        if (lockTable.get(foundIndex) == -1) {
            lockTable.update(foundIndex, state.getId());
        } else {
            state.getExecutionStack().push(this);
        }
        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv)
            throws InterpreterException {
        if (!typeEnv.isDefined(var))
            throw new InterpreterException("Variable not defined!");
        if (typeEnv.getValue(var).equals(new IntegerType()))
            return typeEnv;
        else
            throw new InterpreterException("Variable is not of type int!");
    }

    @Override
    public IStatement deepCopy() {
        return new LockStatement(var);
    }

    @Override
    public String toString() {
        return "lock(" + var + ")";
    }
}
