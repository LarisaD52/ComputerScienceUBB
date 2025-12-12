package model.statement;

import exceptions.MyException;
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

        return new ProgramState(
                newStack,
                newSymTable,
                output,
                innerStatement,
                fileTable,
                heap
        );

    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        // Verifică instrucțiunea internă (un nou fir de execuție pornește cu o copie a mediului de tipuri)
        // Folosim o copie a typeEnv pentru typecheck-ul intern, deși rezultatul nu este folosit
        innerStatement.typecheck(typeEnv.deepCopy());

        // Returnează mediul de tipuri neschimbat
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
