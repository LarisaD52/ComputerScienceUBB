package model.state;

import model.statement.IStatement;
import model.value.IValue;

import java.io.BufferedReader;

public class ProgramState {

    private final IExecutionStack<IStatement> executionStack;
    private final ISymbolTable<String, IValue> symbolTable;
    private final IOut<IValue> output;
    private final IStatement originalProgram;
    private final IFileTable<String, BufferedReader> fileTable;


    // Constructor complet (dacă vrei să treci fileTable extern)
    public ProgramState(IExecutionStack<IStatement> stack,
                        ISymbolTable<String, IValue> symbols,
                        IOut<IValue> outputList,
                        IStatement program,
                        IFileTable<String, BufferedReader> fileTable)  {
        this.executionStack = stack;
        this.symbolTable = symbols;
        this.output = outputList;
        this.originalProgram = program;
        this.fileTable = fileTable;
        this.executionStack.push(program);
    }

    public IExecutionStack<IStatement> getExecutionStack() {
        return executionStack;
    }

    public ISymbolTable<String, IValue> getSymbolTable() {
        return symbolTable;
    }

    public IOut<IValue> getOutput() {
        return output;
    }

    public IStatement getOriginalProgram() {
        return originalProgram;
    }

    public IFileTable<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    @Override
    public String toString() {
        return "\n--- PROGRAM STATE ---" +
                "\nExecution Stack: " + executionStack +
                "\nSymbol Table: " + symbolTable +
                "\nOutput: " + output +
                "\nFileTable: " + fileTable +
                "\n----------------------\n";
    }



}
