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
    public IStatement deepCopy() {
        return new PrintStatement(expression);
        // similar, daca vrei expresia sa fie copiata, adauga deepCopy in IExpression
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
