package model.statement;

import exceptions.MyException;
import model.state.ProgramState;
import model.type.IntegerType;
import model.type.IType;
import model.value.IntegerValue;
import model.value.IValue;
import model.state.ISymbolTable;

public class AwaitStatement implements IStatement {

    private final String var;

    public AwaitStatement(String var) {
        this.var = var;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {

        // 1. verificăm variabila
        if (!state.getSymbolTable().isDefined(var)) {
            throw new MyException("await: variable not defined");
        }

        IValue value = state.getSymbolTable().getValue(var);
        if (!value.getType().equals(new IntegerType())) {
            throw new MyException("await: variable is not int");
        }

        int index = ((IntegerValue) value).value();

        // 2. verificăm existența în LatchTable
        if (!state.getLatchTable().containsKey(index)) {
            throw new MyException("await: invalid latch index");
        }

        // 3. logica de await (atomic, prin LatchTable)
        int current = state.getLatchTable().get(index);

        if (current > 0) {
            // punem statement-ul înapoi pe stack
            state.getExecutionStack().push(this);
        }
        // dacă e 0 -> nu facem nimic, execuția continuă

        return null;
    }

    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {

        IType typeVar = typeEnv.getValue(var);

        if (!typeVar.equals(new IntegerType())) {
            throw new MyException("await: variable is not int");
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
