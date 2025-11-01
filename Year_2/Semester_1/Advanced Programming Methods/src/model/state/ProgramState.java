package model.state;

public class ProgramState {
    private final ExecutionStack executionStack;
    private final SymbolTable symbolTable;

    public ProgramState(ExecutionStack executionStack, SymbolTable symbolTable) {
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public ExecutionStack getExecutionStack() {
        return executionStack;
    }

}
