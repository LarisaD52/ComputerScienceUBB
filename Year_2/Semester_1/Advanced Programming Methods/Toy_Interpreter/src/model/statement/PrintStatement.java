package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.IOut;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;
import model.value.IValue;

public class PrintStatement implements IStatement {
    private final IExpression expression;

    public PrintStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(expression);
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        IOut<IValue> output = state.getOutput();
        output.add(expression.evaluate(state.getSymbolTable(), state.getHeap()));
        return null;
    }



    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        // Verifica tipul expresiei (rezultatul nu conteaza, doar ca e valida)
        expression.typecheck(typeEnv);

        // Returneaza mediul de tipuri neschimbat
        return typeEnv;
    }

    @Override
    public String toString() {
        return "print(" + expression.toString() + ")";
    }
}
