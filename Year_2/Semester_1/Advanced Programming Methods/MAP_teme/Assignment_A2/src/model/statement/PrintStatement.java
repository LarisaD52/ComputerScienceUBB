package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.IOut;
import model.state.ProgramState;
import model.value.IValue;

public class PrintStatement implements IStatement {
    private final IExpression expression;

    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IOut<IValue> output = state.getOutput();
        output.add(expression.evaluate(state.getSymbolTable()));
        return state;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}
