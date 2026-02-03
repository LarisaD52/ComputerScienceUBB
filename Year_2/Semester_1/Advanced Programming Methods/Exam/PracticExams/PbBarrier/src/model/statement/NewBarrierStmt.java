package model.statement;

import exceptions.MyException;
import javafx.util.Pair;
import model.expression.IExpression;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntegerType;
import model.value.IValue;
import model.value.IntegerValue;
import model.state.IBarrierTable;

import java.util.ArrayList;

public class NewBarrierStmt implements IStatement {

    private final String var;
    private final IExpression exp;

    public NewBarrierStmt(String var, IExpression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        ISymbolTable<String, IValue> symTable = state.getSymbolTable();
        IBarrierTable barrierTable = state.getBarrierTable();

        // 1️⃣ variabila trebuie să existe
        if (!symTable.isDefined(var)) {
            throw new MyException("newBarrier: variable not defined");
        }

        IValue varValue = symTable.getValue(var);
        if (!varValue.getType().equals(new IntegerType())) {
            throw new MyException("newBarrier: variable not of type int");
        }

        // 2️⃣ evaluăm expresia
        IValue expValue = exp.evaluate(symTable, state.getHeap());
        if (!expValue.getType().equals(new IntegerType())) {
            throw new MyException("newBarrier: expression not of type int");
        }

        int N = ((IntegerValue) expValue).value();

        // 3️⃣ obținem index liber
        int index = barrierTable.getFreeLocation();

        // 4️⃣ adăugăm bariera
        barrierTable.put(index, new Pair<>(N, new ArrayList<>()));

        // 5️⃣ actualizăm variabila
        symTable.update(var, new IntegerValue(index));

        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {

        IType varType = typeEnv.getValue(var);
        IType expType = exp.typecheck(typeEnv);

        if (!varType.equals(new IntegerType())) {
            throw new MyException("newBarrier: var is not int");
        }

        if (!expType.equals(new IntegerType())) {
            throw new MyException("newBarrier: exp is not int");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new NewBarrierStmt(var, exp.deepCopy());
    }

    @Override
    public String toString() {
        return "newBarrier(" + var + ", " + exp + ")";
    }
}
