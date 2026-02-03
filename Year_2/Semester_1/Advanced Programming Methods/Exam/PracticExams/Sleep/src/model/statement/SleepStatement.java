package model.statement;

import exceptions.MyException;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.state.IExecutionStack;
import model.type.IType;

public class SleepStatement implements IStatement {

    private final int number;

    public SleepStatement(int number) {
        this.number = number;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IExecutionStack<IStatement> stack = state.getExecutionStack();

        if (number > 0) {
            stack.push(new SleepStatement(number - 1));
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new SleepStatement(number);
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "sleep(" + number + ")";
    }
}
