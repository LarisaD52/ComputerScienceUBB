package model.expression;

import exceptions.MyException;
import model.state.ISymbolTable;
import model.state.IHeap;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;



public class ReadHeapExpression implements IExpression {
    private final IExpression expression;

    public ReadHeapExpression(IExpression expression) {
        this.expression = expression;
    }


    @Override
    public IValue evaluate(ISymbolTable<String, IValue> symbolTable, IHeap heap) throws MyException {
        IValue value = expression.evaluate(symbolTable, heap);
        if (!(value instanceof RefValue ref))
            throw new MyException("rH: expression is not a RefValue");

        int addr = ref.getAddr();

        // Fix: throw if address not in heap
        if (!heap.containsKey(addr))
            throw new MyException("rH: invalid heap address " + addr);

        return heap.read(addr);
    }


    @Override
    public IExpression deepCopy() {
        return new ReadHeapExpression(expression.deepCopy());
    }

    @Override
    public IType typecheck(ISymbolTable<String,IType> typeEnv) throws MyException{
        IType typ = expression.typecheck(typeEnv);
        if (typ instanceof RefType refType) {
            return refType.getInner();
        } else
            throw new MyException("The rH argument is not a Ref Type.");
    }

    @Override
    public String toString() {
        return "rH(" + expression + ")";
    }
}
