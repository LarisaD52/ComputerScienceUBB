package model.state;

import model.statement.IStatement;
import model.value.IValue;
import exceptions.MyException;

public class ProgramState {

    // Variabile de instanță (mai clare)
    private final IExecutionStack<IStatement> executionStack;
    private final ISymbolTable<String, IValue> symbolTable;
    private final IOut<IValue> output;
    private final IStatement originalProgram;

    // Constructor
    public ProgramState(IExecutionStack<IStatement> stack,
                        ISymbolTable<String, IValue> symbols,
                        IOut<IValue> outputList,
                        IStatement program) throws MyException {
        // atribuim parametrii la variabilele de instanță
        this.executionStack = stack;
        this.symbolTable = symbols;
        this.output = outputList;
        this.originalProgram = program; // presupunem deep copy deja realizat

        // punem programul original pe stiva de execuție
        this.executionStack.push(program);
    }

    // Getter pentru stiva de execuție
    public IExecutionStack<IStatement> getExecutionStack() {
        return executionStack;
    }

    // Getter pentru tabela de simboluri
    public ISymbolTable<String, IValue> getSymbolTable() {
        return symbolTable;
    }

    // Getter pentru lista de output
    public IOut<IValue> getOutput() {
        return output;
    }

    // Getter pentru programul original
    public IStatement getOriginalProgram() {
        return originalProgram;
    }
    @Override
    public String toString() {
        return "\n--- PROGRAM STATE ---" +
                "\nExecution Stack: " + executionStack +
                "\nSymbol Table: " + symbolTable +
                "\nOutput: " + output +
                "\n----------------------\n";
    }

}
