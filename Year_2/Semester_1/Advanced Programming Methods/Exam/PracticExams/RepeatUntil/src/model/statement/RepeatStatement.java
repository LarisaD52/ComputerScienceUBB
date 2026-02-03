package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.expression.NotExpression;
import model.state.IExecutionStack;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.IType;

public class RepeatStatement implements IStatement {

    private final IStatement stmt;
    private final IExpression exp;

    public RepeatStatement(IStatement stmt, IExpression exp) {
        this.stmt = stmt;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        // repeat S until E
        // == S; while (!E) S

        IExecutionStack<IStatement> stack = state.getExecutionStack();

        IStatement transformed =
                new CompoundStatement(
                        stmt.deepCopy(),
                        new WhileStatement(
                                new NotExpression(exp.deepCopy()),
                                stmt.deepCopy()
                        )
                );

        stack.push(transformed);

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new RepeatStatement(stmt.deepCopy(), exp.deepCopy());
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {

        IType expType = exp.typecheck(typeEnv);

        if (!expType.equals(new BooleanType()))
            throw new MyException("Repeat condition is not boolean!");

        stmt.typecheck(typeEnv.deepCopy());

        return typeEnv;
    }

    @Override
    public String toString() {
        return "repeat(" + stmt + ") until(" + exp + ")";
    }
}
