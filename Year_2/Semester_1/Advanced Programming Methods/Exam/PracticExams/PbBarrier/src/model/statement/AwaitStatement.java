package model.statement;

import exceptions.MyException;
import javafx.util.Pair;
import model.state.IBarrierTable;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntegerType;
import model.value.IValue;
import model.value.IntegerValue;

import java.util.List;

public class AwaitStatement implements IStatement {

    private final String var;

    public AwaitStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        ISymbolTable<String, IValue> symTable = state.getSymbolTable();
        IBarrierTable barrierTable = state.getBarrierTable();

        // 1️⃣ variabila trebuie să existe
        if (!symTable.isDefined(var)) {
            throw new MyException("await: variable not defined");
        }

        IValue varValue = symTable.getValue(var);
        if (!varValue.getType().equals(new IntegerType())) {
            throw new MyException("await: variable not of type int");
        }

        int foundIndex = ((IntegerValue) varValue).value();//nu folosesc getValue() pt ca clasa mea IValue e record si atunci nu i nevoie de getter

        // 2️⃣ index-ul trebuie să existe în BarrierTable
        if (!barrierTable.containsKey(foundIndex)) {
            throw new MyException("await: barrier index not found");
        }

        // 3️⃣ obținem bariera
        Pair<Integer, List<Integer>> barrier = barrierTable.get(foundIndex);
        int N = barrier.getKey();
        List<Integer> list = barrier.getValue();

        int currentId = state.getId();

        // 4️⃣ logica de sincronizare
        if (N > list.size()) {

            if (!list.contains(currentId)) {
                list.add(currentId);
                barrierTable.update(foundIndex, new Pair<>(N, list));
            }

            // punem await înapoi pe stack
            state.getExecutionStack().push(this);
        }

        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {

        IType varType = typeEnv.getValue(var);
        if (!varType.equals(new IntegerType())) {
            throw new MyException("await: var is not int");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new AwaitStatement(var);
    }

    @Override
    public String toString() {
        return "await(" + var + ")";
    }
}
