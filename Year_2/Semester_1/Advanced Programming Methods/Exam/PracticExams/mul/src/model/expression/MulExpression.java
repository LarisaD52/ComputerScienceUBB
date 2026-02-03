package model.expression;
import exceptions.MyException;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.IntegerType;
import model.type.IType;
import model.value.IValue;
import model.value.IntegerValue;

public class MulExpression implements IExpression {
    private final IExpression exp1;
    private final IExpression exp2;

    public MulExpression(IExpression exp1, IExpression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public IValue evaluate(ISymbolTable symTable, IHeap heap) throws MyException {
        IValue v1 = exp1.evaluate(symTable, heap);
        IValue v2 = exp2.evaluate(symTable, heap);

        if (!v1.getType().equals(new IntegerType()) ||
                !v2.getType().equals(new IntegerType())) {
            throw new MyException("MUL requires integer operands");
        }

        int n1 = ((IntegerValue) v1).value();
        int n2 = ((IntegerValue) v2).value();

        return new IntegerValue(n1 * n2 - (n1 + n2));
    }

    @Override
    public IType typecheck(ISymbolTable typeEnv) throws MyException {
        IType t1 = exp1.typecheck(typeEnv);
        IType t2 = exp2.typecheck(typeEnv);

        if (t1.equals(new IntegerType()) && t2.equals(new IntegerType())) {
            return new IntegerType();
        }
        throw new MyException("MUL type error");
    }

    @Override
    public String toString() {
        return "MUL(" + exp1 + "," + exp2 + ")";
    }

    @Override
    public IExpression deepCopy() {
        return new MulExpression(
                exp1.deepCopy(),
                exp2.deepCopy()
        );
    }

}
