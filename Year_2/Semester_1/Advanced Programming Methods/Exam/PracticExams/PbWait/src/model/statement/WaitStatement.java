package model.statement;
import exceptions.MyException;
import model.expression.ConstantExpression;
import model.expression.IExpression;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;
import model.state.ISymbolTable;
import model.type.IType;
import exceptions.MyException;
import model.value.IntegerValue;

public class WaitStatement implements IStatement {

    private final int number;

    public WaitStatement(int number) {
        this.number = number;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        if (number > 0) {
            state.getExecutionStack().push(
                    new CompoundStatement(
                            new PrintStatement(
                                    new ConstantExpression(
                                            new IntegerValue(number)
                                    )
                            ),
                            new WaitStatement(number - 1)
                    )
            );
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new WaitStatement(number);
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }


    @Override
    public String toString() {
        return "wait(" + number + ")";
    }
}
