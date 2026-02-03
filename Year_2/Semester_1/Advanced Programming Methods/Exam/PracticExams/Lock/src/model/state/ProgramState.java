package model.state;

import exceptions.EmptyExecutionStackException;
import exceptions.MyException;
import model.statement.IStatement;
import model.type.BooleanType;
import model.value.IValue;
import model.state.ILockTable;
import model.state.LockTable;

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
    private IHeap heap; // final ca sa fie mereu disponibil
    private final ILockTable lockTable;


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
        this.heap = new Heap();
        this.lockTable = new LockTable();
        this.executionStack.push(program);
        this.id = getNewId();
    }

    public ProgramState(
            IExecutionStack<IStatement> stack,
            ISymbolTable<String, IValue> symbols,
            IOut<IValue> output,
            IStatement program,
            IFileTable<String, BufferedReader> fileTable,
            IHeap heap,
            ILockTable lockTable

    ) {
        this.executionStack = stack;
        this.symbolTable = symbols;
        this.output = output;
        this.originalProgram = program;
        this.fileTable = fileTable;
        this.heap = heap;
        this.lockTable = lockTable;
        this.id = getNewId();
    }


    public ILockTable getLockTable() {
        return lockTable;
    }



    public int getId() {
        return id;
    }

    public void setHeap(IHeap heap) {
        this.heap = heap;
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

    public IHeap getHeap() {
        return heap;
    }

    public Boolean isNotCompleted(){
        if (executionStack.isEmpty()){
            return false;
        }
        return true;
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
