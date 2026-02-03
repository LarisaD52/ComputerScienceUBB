package model.statement;

import exceptions.MyException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.state.ProgramState;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;
import model.expression.IExpression;

public class WriteHeapStatement implements IStatement {
    private final String varName;
    private final IExpression expression;

    public WriteHeapStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        var symTable = state.getSymbolTable();
        IHeap heap = state.getHeap();

        if (!symTable.isDefined(varName))
            throw new MyException(varName + " is not defined.");

        IValue varValue = symTable.getValue(varName);

        if (!(varValue instanceof RefValue ref))
            throw new MyException(varName + " is not of RefType.");

        int addr = ref.getAddr();

        //check if heap contains address
        if (!heap.containsKey(addr))
            throw new MyException("Invalid heap address: " + addr);


        IValue evalValue = expression.evaluate(symTable, heap);

        if (!evalValue.getType().equals(ref.getLocationType()))
            throw new MyException("Type mismatch in wH: " + varName);

        heap.write(addr, evalValue);

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new WriteHeapStatement(varName, expression.deepCopy());
    }



    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        //obtine tipul variabilei (trebuie sa fie RefType)
        IType typeVar = typeEnv.getValue(varName);

        //obtine tipul expresiei
        IType typeExp = expression.typecheck(typeEnv);

        if (typeVar instanceof RefType refType) {
            //verifica daca tipul interior al referintei se potriveste cu tipul expresiei
            if (refType.getInner().equals(typeExp)) {
                return typeEnv; //tipurile se potrivesc
            } else {
                throw new MyException("WH stmt: right hand side (" + typeExp +
                        ") and left hand side (Ref of " + refType.getInner() +
                        ") have different inner types.");
            }
        } else {
            throw new MyException("WH stmt: Variable " + varName + " is not of RefType.");
        }
    }
    @Override
    public String toString() {
        return "wH(" + varName + ", " + expression + ")";
    }
}
