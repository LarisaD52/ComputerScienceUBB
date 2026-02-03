package model.statement;

import exceptions.MyException;
import model.state.*;
import model.type.IType;
import model.type.IntegerType;
import model.value.IValue;
import model.value.IntegerValue;
import javafx.util.Pair;

import java.util.List;

public class ReleaseStatement implements IStatement {

    private final String var;

    public ReleaseStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        ISymbolTable<String, IValue> symTable = state.getSymbolTable();
        ISemaphoreTable semaphoreTable = state.getSemaphoreTable();

        // 1. variabila trebuie să existe
        if (!symTable.isDefined(var))
            throw new MyException("Release: variable not defined: " + var);

        IValue value = symTable.getValue(var);

        // 2. variabila trebuie să fie int
        if (!(value instanceof IntegerValue))
            throw new MyException("Release: variable not of type int");

        int semaphoreAddress = ((IntegerValue) value).value();

        // 3. adresa trebuie să existe în semaphoreTable
        if (!semaphoreTable.containsKey(semaphoreAddress))
            throw new MyException("Release: invalid semaphore address");

        // 4. logica release
        Pair<Integer, List<Integer>> entry =
                semaphoreTable.read(semaphoreAddress);

        List<Integer> acquiredList = entry.getValue();
        int currentThreadId = state.getId();

        if (acquiredList.contains(currentThreadId)) {
            acquiredList.remove((Integer) currentThreadId);
            semaphoreTable.write(
                    semaphoreAddress,
                    entry
            );
        }

        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv)
            throws MyException {

        if (!typeEnv.containsKey(var))
            throw new MyException("Release: variable not defined");

        if (!typeEnv.getValue(var).equals(new IntegerType()))
            throw new MyException("Release: variable must be int");

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ReleaseStatement(var);
    }

    @Override
    public String toString() {
        return "release(" + var + ")";
    }
}
