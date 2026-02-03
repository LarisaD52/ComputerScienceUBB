package model.state;

import exceptions.EmptyExecutionStackException;
import exceptions.MyException;
import model.statement.IStatement;
import model.value.IValue;

import java.io.BufferedReader;

public class ProgramState {

    private static int lastId = 0;

    private static synchronized int getNewId() {
        lastId++;
        return lastId;
    }

    private final int id;
    private final IExecutionStack<IStatement> executionStack;
    private final ISymbolTable<String, IValue> symbolTable;
    private final IOut<IValue> output;
    private final IStatement originalProgram;
    private final IFileTable<String, BufferedReader> fileTable;
    private final IHeap heap;
    private final ILatchTable latchTable;

    // program initial
    public ProgramState(
            IExecutionStack<IStatement> stack,
            ISymbolTable<String, IValue> symbols,
            IOut<IValue> outputList,
            IStatement program,
            IFileTable<String, BufferedReader> fileTable
    ) {
        this.executionStack = stack;
        this.symbolTable = symbols;
        this.output = outputList;
        this.originalProgram = program;
        this.fileTable = fileTable;
        this.heap = new Heap();
        this.latchTable = new LatchTable();
        this.executionStack.push(program);
        this.id = getNewId();
    }

    // fork
    public ProgramState(
            IExecutionStack<IStatement> stack,
            ISymbolTable<String, IValue> symbols,
            IOut<IValue> output,
            IStatement program,
            IFileTable<String, BufferedReader> fileTable,
            IHeap heap,
            ILatchTable latchTable
    ) {
        this.executionStack = stack;
        this.symbolTable = symbols;
        this.output = output;
        this.originalProgram = program;
        this.fileTable = fileTable;
        this.heap = heap;
        this.latchTable = latchTable;
        this.id = getNewId();
    }

    public int getId() {
        return id;
    }

    public IStatement getOriginalProgram() {
        return originalProgram;
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

    public IFileTable<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public IHeap getHeap() {
        return heap;
    }

    public ILatchTable getLatchTable() {
        return latchTable;
    }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    public ProgramState oneStep() throws MyException {
        if (executionStack.isEmpty())
            throw new EmptyExecutionStackException("Execution stack is empty!");
        IStatement current = executionStack.pop();
        return current.execute(this);
    }

    @Override
    public String toString() {
        return "\n--- PROGRAM STATE " + id + "---" +
                "\nExecution Stack: " + executionStack +
                "\nSymbol Table: " + symbolTable +
                "\nHeap: " + heap +
                "\nOutput: " + output +
                "\nFileTable: " + fileTable +
                "\n----------------------\n";
    }
}
