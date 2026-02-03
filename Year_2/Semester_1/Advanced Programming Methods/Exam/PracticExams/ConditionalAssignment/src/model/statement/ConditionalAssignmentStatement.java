package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.IExecutionStack;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.BooleanType;
import model.type.IType;

public class ConditionalAssignmentStatement implements IStatement {

    private final String varName;
    private final IExpression exp1;
    private final IExpression exp2;
    private final IExpression exp3;

    public ConditionalAssignmentStatement(String varName,
                                          IExpression exp1,
                                          IExpression exp2,
                                          IExpression exp3) {
        this.varName = varName;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        // v = exp1 ? exp2 : exp3
        // =>
        // if (exp1) then v=exp2 else v=exp3

        IExecutionStack<IStatement> stack = state.getExecutionStack();

        IStatement transformed =
                new IfStatement(
                        exp1.deepCopy(),
                        new AssignmentStatement(varName, exp2.deepCopy()),
                        new AssignmentStatement(varName, exp3.deepCopy())
                );

        stack.push(transformed);

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new ConditionalAssignmentStatement(
                varName,
                exp1.deepCopy(),
                exp2.deepCopy(),
                exp3.deepCopy()
        );
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {

        // exp1 trebuie bool
        IType type1 = exp1.typecheck(typeEnv);
        if (!type1.equals(new BooleanType())) {
            throw new MyException("Conditional assignment: condition is not boolean.");
        }

        // variabila trebuie sa fie definita
        if (!typeEnv.isDefined(varName)) {
            throw new MyException("Conditional assignment: variable not defined.");
        }

        IType varType = typeEnv.getValue(varName);

        // exp2 si exp3 trebuie sa aiba acelasi tip ca variabila
        IType type2 = exp2.typecheck(typeEnv);
        IType type3 = exp3.typecheck(typeEnv);

        if (!type2.equals(varType) || !type3.equals(varType)) {
            throw new MyException("Conditional assignment: types do not match.");
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return varName + " = (" + exp1 + " ? " + exp2 + " : " + exp3 + ")";
    }
}
