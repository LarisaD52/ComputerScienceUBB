package model.statement;

import exceptions.MyException;
import model.state.*;
import model.type.IType;
import model.type.IntegerType;
import model.value.IValue;
import javafx.util.Pair;
import model.value.IntegerValue;

import java.util.List;

public class AcquireStatement implements IStatement {

    private final String var;

    public AcquireStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        ISymbolTable<String, IValue> symTable = state.getSymbolTable();
        ISemaphoreTable semaphoreTable = state.getSemaphoreTable();
        IExecutionStack<IStatement> exeStack = state.getExecutionStack();

        // 1. var trebuie să existe
        if (!symTable.containsKey(var))
            throw new MyException("Acquire: variable not defined: " + var);

        IValue value = symTable.getValue(var);

        // 2. var trebuie să fie int
        if (!(value instanceof IntegerValue))
            throw new MyException("Acquire: variable not of type int");

        int semaphoreAddress = ((IntegerValue) value).value();

        // 3. trebuie să existe în semaphoreTable
        if (!semaphoreTable.containsKey(semaphoreAddress))
            throw new MyException("Acquire: invalid semaphore address");

        // 4. logica de acquire (atomic – lock e în SemaphoreTable)
        Pair<Integer, List<Integer>> semaphoreEntry =
                semaphoreTable.read(semaphoreAddress);

        int permits = semaphoreEntry.getKey();
        List<Integer> acquiredList = semaphoreEntry.getValue();
        int currentThreadId = state.getId();

        if (permits > acquiredList.size()) {
            if (!acquiredList.contains(currentThreadId)) {
                acquiredList.add(currentThreadId);
                semaphoreTable.write(
                        semaphoreAddress,
                        new Pair<>(permits, acquiredList)
                );
            }
        } else {
            // nu sunt permise disponibile → block
            exeStack.push(this);
        }

        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv)
            throws MyException {

        if (!typeEnv.containsKey(var))
            throw new MyException("Acquire: variable not defined");

        if (!typeEnv.getValue(var).equals(new IntegerType()))
            throw new MyException("Acquire: variable must be int");

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new AcquireStatement(var);
    }

    @Override
    public String toString() {
        return "acquire(" + var + ")";
    }
}
