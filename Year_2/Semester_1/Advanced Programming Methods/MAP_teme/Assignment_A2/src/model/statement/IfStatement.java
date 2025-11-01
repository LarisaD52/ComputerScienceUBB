package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.IExecutionStack;
import model.state.ProgramState;
import model.value.BooleanValue;
import model.value.IValue;

public class IfStatement implements IStatement {
    private final IExpression condition;
    private final IStatement thenStatement;
    private final IStatement elseStatement;

    public IfStatement(IExpression condition, IStatement thenStatement, IStatement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IExecutionStack<IStatement> stack = state.getExecutionStack();
        IValue conditionValue = condition.evaluate(state.getSymbolTable());

        if (!(conditionValue instanceof BooleanValue boolVal))
            throw new MyException("Condition in IF is not a boolean expression.");

        if (boolVal.value())
            stack.push(thenStatement);
        else
            stack.push(elseStatement);

        return state;
    }

    @Override
    public String toString() {
        return "(IF(" + condition + ") THEN(" + thenStatement + ") ELSE(" + elseStatement + "))";
    }
}
