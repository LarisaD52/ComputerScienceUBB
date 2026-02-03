package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.expression.RelationalExpression;
import model.state.ProgramState;
import model.state.ISymbolTable;
import model.state.IHeap;
import model.type.IType;

public class SwitchStatement implements IStatement {

    private final IExpression exp;
    private final IExpression exp1;
    private final IExpression exp2;

    private final IStatement caseStmt1;
    private final IStatement caseStmt2;
    private final IStatement defaultStmt;

    public SwitchStatement(IExpression exp,
                           IExpression exp1,
                           IExpression exp2,
                           IStatement caseStmt1,
                           IStatement caseStmt2,
                           IStatement defaultStmt) {
        this.exp = exp;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.caseStmt1 = caseStmt1;
        this.caseStmt2 = caseStmt2;
        this.defaultStmt = defaultStmt;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        state.getExecutionStack().push(
                new IfStatement(
                        new RelationalExpression("==", exp, exp1),
                        caseStmt1,
                        new IfStatement(
                                new RelationalExpression("==", exp, exp2),
                                caseStmt2,
                                defaultStmt
                        )
                )
        );

        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv)
            throws MyException {

        IType tExp = exp.typecheck(typeEnv);
        IType tExp1 = exp1.typecheck(typeEnv);
        IType tExp2 = exp2.typecheck(typeEnv);

        if (!tExp.equals(tExp1) || !tExp.equals(tExp2)) {
            throw new MyException("Switch expressions must have same type!");
        }

        caseStmt1.typecheck(typeEnv.deepCopy());
        caseStmt2.typecheck(typeEnv.deepCopy());
        defaultStmt.typecheck(typeEnv.deepCopy());

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new SwitchStatement(
                exp.deepCopy(),
                exp1.deepCopy(),
                exp2.deepCopy(),
                caseStmt1.deepCopy(),
                caseStmt2.deepCopy(),
                defaultStmt.deepCopy()
        );
    }
}
