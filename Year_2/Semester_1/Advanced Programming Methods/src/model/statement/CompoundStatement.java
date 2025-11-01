package model.statement;

import model.state.ExecutionStack;
import model.state.ProgramState;

public class CompoundStatement implements Statement{
    private final Statement first;
    private final Statement second;

    public CompoundStatement(Statement first, Statement second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        var stack = state.getExecutionStack();
        stack.push(second);
        stack.push(first);
        return state;
    }
}
