package model.statement;

import model.expression.IExpression;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.value.IntegerValue;
import model.value.IValue;
import exceptions.MyException;

import java.util.Map;

public class NewLatchStmt implements IStatement {

    private final String var;
    private final IExpression exp;

    public NewLatchStmt(String var, IExpression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        // 1. evaluăm expresia
        IValue value = exp.evaluate(state.getSymbolTable(), state.getHeap());

        if (!value.getType().equals(new IntegerType())) {
            throw new MyException("newLatch: expression is not int");
        }

        int number = ((IntegerValue) value).value();

        // 2. adăugăm în LatchTable
        int location = state.getLatchTable().add(number);

        // 3. punem locația în variabilă
        if (!state.getSymbolTable().isDefined(var)) {
            throw new MyException("newLatch: variable not defined");
        }

        IValue varValue = state.getSymbolTable().getValue(var);
        if (!varValue.getType().equals(new IntegerType())) {
            throw new MyException("newLatch: variable is not int");
        }

        state.getSymbolTable().update(var, new IntegerValue(location));

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new NewLatchStmt(var, exp.deepCopy());
    }


    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {

        IType typeVar = typeEnv.getValue(var);
        IType typeExp = exp.typecheck(typeEnv);

        if (!typeVar.equals(new IntegerType())) {
            throw new MyException("newLatch: variable is not int");
        }

        if (!typeExp.equals(new IntegerType())) {
            throw new MyException("newLatch: expression is not int");
        }

        return typeEnv;
    }


    @Override
    public String toString() {
        return "newLatch(" + var + ", " + exp + ")";
    }
}
