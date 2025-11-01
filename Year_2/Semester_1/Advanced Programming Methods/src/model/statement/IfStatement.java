package model.statement;

import model.expression.Expression;
import model.state.ProgramState;
import model.value.BooleanValue;
import model.value.Value;

public class IfStatement implements Statement {
    private final Expression condition;
    private final Statement thenBranch;
    private final Statement elseBranch;

    public IfStatement(Expression condition, Statement thenBranch, Statement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        Value result = condition.evaluate(state.getSymbolTable());
        if (result instanceof BooleanValue booleanValue) {
            if (booleanValue.getValue()) {
                state.getExecutionStack().push(thenBranch);
            } else {
                state.getExecutionStack().push(elseBranch);
            }
        } else {
            throw new RuntimeException("Condition expression does not evaluate to a boolean.");
        }
        return state;
    }


}
