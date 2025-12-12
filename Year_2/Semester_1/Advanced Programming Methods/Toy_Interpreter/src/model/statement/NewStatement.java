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

public class NewStatement implements IStatement {
    private final String varName;
    private final IExpression expression;

    public NewStatement(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        ISymbolTable<String, IValue> symTable = state.getSymbolTable();
        IHeap heap = state.getHeap();

        if (!symTable.isDefined(varName)) {
            throw new MyException(varName + " is not defined in the symbol table.");
        }

        IValue varValue = symTable.getValue(varName);
        if (!(varValue instanceof RefValue ref)) {
            throw new MyException(varName + " is not of RefType.");
        }

        IValue evalValue = expression.evaluate(symTable, heap);

        if (!evalValue.getType().equals(ref.getLocationType())) {
            throw new MyException("Type mismatch: variable " + varName +
                    " has RefType(" + ref.getLocationType() + "), but expression evaluates to " + evalValue.getType());
        }

        //alocam in heap
        int addr = heap.allocate(evalValue);

        //actualizarea simbol table
        symTable.update(varName, new RefValue(addr, ref.getLocationType()));

        return null;
    }



    @Override
    public ISymbolTable<String, IType> typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        // 1. Obtine tipul variabilei din SymbolTable (trebuie sa fie RefType)
        IType typeVar = typeEnv.getValue(varName);

        // 2. Obtine tipul expresiei
        IType typeExp = expression.typecheck(typeEnv);

        if (typeVar instanceof RefType refType) {
            // 3. Verifica daca tipul interior al referintei (refType.getInner()) se potriveste cu tipul expresiei (typeExp)
            if (refType.getInner().equals(typeExp)) {
                return typeEnv; // Tipurile se potrivesc
            } else {
                throw new MyException("NEW stmt: right hand side (" + typeExp +
                        ") and left hand side (Ref of " + refType.getInner() +
                        ") have different inner types.");
            }
        } else {
            throw new MyException("NEW stmt: Variable " + varName + " is not of RefType.");
        }
    }
    @Override
    public IStatement deepCopy() {
        return new NewStatement(varName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + varName + ", " + expression + ")";
    }
}
