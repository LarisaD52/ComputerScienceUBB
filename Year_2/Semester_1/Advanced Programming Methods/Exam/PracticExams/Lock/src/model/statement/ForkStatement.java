package model.statement;

import exceptions.MyException;
import model.state.ILockTable;
import model.state.*;
import model.type.IType;
import model.value.IValue;

import java.io.BufferedReader;

public class ForkStatement implements IStatement {

    private final IStatement innerStatement;

    public ForkStatement(IStatement stmt) {
        this.innerStatement = stmt;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        IExecutionStack<IStatement> newStack = new StackExecutionStack<>();
        newStack.push(innerStatement.deepCopy());

        ISymbolTable<String, IValue> newSymTable = state.getSymbolTable().deepCopy();
        IOut<IValue> output = state.getOutput();
        IFileTable<String, BufferedReader> fileTable = state.getFileTable();
        IHeap heap = state.getHeap();
        ILockTable lockTable = state.getLockTable();

        return new ProgramState(
                newStack,
                newSymTable,
                output,
                innerStatement,
                fileTable,
                heap,
                lockTable
        );
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        innerStatement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(innerStatement.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + innerStatement.toString() + ")";
    }
}
