package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.expression.RelationalExpression;
import model.expression.VariableExpression;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntegerType;
import model.state.ISymbolTable;

public class ForStatement implements IStatement {

    private final String varName;
    private final IExpression exp1;
    private final IExpression exp2;
    private final IExpression exp3;
    private final IStatement stmt;

    public ForStatement(String varName,
                        IExpression exp1,
                        IExpression exp2,
                        IExpression exp3,
                        IStatement stmt) {
        this.varName = varName;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.stmt = stmt;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

    IStatement transformed =
        new CompoundStatement(
            new VariableDeclarationStatement(varName, new IntegerType()),
            new CompoundStatement(
                new AssignmentStatement(varName, exp1),
                new WhileStatement(
                    new RelationalExpression("<", new VariableExpression(varName), exp2),
                    new CompoundStatement(stmt, new AssignmentStatement(varName, exp3)))));

        state.getExecutionStack().push(transformed);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new ForStatement(
                varName,
                exp1.deepCopy(),
                exp2.deepCopy(),
                exp3.deepCopy(),
                stmt.deepCopy()
        );
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {

        ISymbolTable<String, IType> newEnv = typeEnv.deepCopy();
        newEnv.put(varName, new IntegerType());

        IType type1 = exp1.typecheck(newEnv);
        IType type2 = exp2.typecheck(newEnv);
        IType type3 = exp3.typecheck(newEnv);

        if (!type1.equals(new IntegerType()))
            throw new MyException("For exp1 is not int");
        if (!type2.equals(new IntegerType()))
            throw new MyException("For exp2 is not int");
        if (!type3.equals(new IntegerType()))
            throw new MyException("For exp3 is not int");

        stmt.typecheck(newEnv);
        return typeEnv;
    }



    @Override
    public String toString() {
        return "for(" + varName + "=" + exp1 + "; " + varName + "<" + exp2 + "; " + varName + "=" + exp3 + ") " + stmt;
    }
}
