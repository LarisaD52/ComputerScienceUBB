package model.statement;

import exceptions.MyException;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.value.IntegerValue;
import model.value.IValue;
import model.state.ILatchTable;

import java.util.Map;

public class CountDownStmt implements IStatement {

    private final String var;

    public CountDownStmt(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        // 1. verificăm variabila
        if (!state.getSymbolTable().isDefined(var)) {
            throw new MyException("countDown: variable not defined");
        }

        IValue value = state.getSymbolTable().getValue(var);
        if (!value.getType().equals(new IntegerType())) {
            throw new MyException("countDown: variable is not int");
        }

        int index = ((IntegerValue) value).value();

        // 2. verificăm existența în LatchTable
        if (!state.getLatchTable().containsKey(index)) {
            throw new MyException("countDown: invalid latch index");
        }

        // 3. decrementare atomică + output
        int current = state.getLatchTable().get(index);

        if (current > 0) {
            state.getLatchTable().update(index, current - 1);
        }

        // se afișează mereu id-ul thread-ului
        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {

        IType typeVar = typeEnv.getValue(var);

        if (!typeVar.equals(new IntegerType())) {
            throw new MyException("countDown: variable is not int");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new CountDownStmt(var);
    }

    @Override
    public String toString() {
        return "countDown(" + var + ")";
    }
}
