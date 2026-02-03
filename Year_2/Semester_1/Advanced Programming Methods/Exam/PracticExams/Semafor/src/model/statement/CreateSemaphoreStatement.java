package model.statement;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.*;
import model.type.IType;
import model.type.IntegerType;
import model.value.IValue;
import model.value.IntegerValue;
import javafx.util.Pair;

import java.util.ArrayList;

public class CreateSemaphoreStatement implements IStatement {

    private final String var;
    private final IExpression expression;

    public CreateSemaphoreStatement(String var, IExpression expression) {
        this.var = var;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        ISymbolTable<String, IValue> symTable = state.getSymbolTable();
        ISemaphoreTable semaphoreTable = state.getSemaphoreTable();
        IHeap heap = state.getHeap();

        // 1. variabila trebuie să existe
        if (!symTable.isDefined(var))
            throw new MyException("CreateSemaphore: variable not defined: " + var);

        IValue varValue = symTable.getValue(var);

        // 2. variabila trebuie să fie int
        if (!(varValue instanceof IntegerValue))
            throw new MyException("CreateSemaphore: variable must be int");

        // 3. evaluăm expresia
        IValue evalValue = expression.evaluate(symTable, heap);

        if (!(evalValue instanceof IntegerValue))
            throw new MyException("CreateSemaphore: expression not int");

        int numberOfPermits = ((IntegerValue) evalValue).value();// nu avem nevoie de un get value pentru ca clasa integervalue e un record

        // 4. alocăm în semaphore table
        int address = semaphoreTable.allocate(
                new Pair<>(numberOfPermits, new ArrayList<>())
        );

        // 5. punem adresa în variabilă
        symTable.update(var, new IntegerValue(address));

        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv)
            throws MyException {

        if (!typeEnv.containsKey(var))
            throw new MyException("CreateSemaphore: variable not defined");

        if (!typeEnv.getValue(var).equals(new IntegerType()))
            throw new MyException("CreateSemaphore: variable must be int");

        IType expType = expression.typecheck(typeEnv);

        if (!expType.equals(new IntegerType()))
            throw new MyException("CreateSemaphore: expression must be int");

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new CreateSemaphoreStatement(var, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "createSemaphore(" + var + ", " + expression + ")";
    }
}
