package model.expression;

import exceptions.MyException;
import model.expression.IExpression;
import model.state.IHeap;
import model.state.ISymbolTable;
import model.type.BooleanType;
import model.type.IType;
import model.value.BooleanValue;
import model.value.IValue;

public class NotExpression implements IExpression {

    private final IExpression exp;

    public NotExpression(IExpression exp) {
        this.exp = exp;
    }

    @Override
    public IValue evaluate(ISymbolTable<String, IValue> symTable, IHeap heap) throws MyException {
        IValue val = exp.evaluate(symTable, heap);

        if (!(val instanceof BooleanValue boolVal))
            throw new MyException("Operand is not boolean!");

        return new BooleanValue(!boolVal.value());
    }

    @Override
    public IType typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        IType type = exp.typecheck(typeEnv);

        if (!type.equals(new BooleanType()))
            throw new MyException("Operand of NOT is not boolean!");

        return new BooleanType();
    }

    @Override
    public IExpression deepCopy() {
        return new NotExpression(exp.deepCopy());
    }

    @Override
    public String toString() {
        return "!(" + exp + ")";
    }
}
