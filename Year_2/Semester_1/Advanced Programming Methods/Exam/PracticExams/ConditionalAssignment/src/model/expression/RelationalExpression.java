package model.expression;

import exceptions.MyException;
import exceptions.TypeMismatchException;
import exceptions.UndefinedOperatorException;
import model.state.ISymbolTable;
import model.state.IHeap;
import model.type.BooleanType;
import model.type.IType;
import model.type.IntegerType;
import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntegerValue;

import java.lang.reflect.Type;

public record RelationalExpression(String operator, IExpression left, IExpression right) implements IExpression {

    @Override
    public IValue evaluate(ISymbolTable<String, IValue> symbolTable, IHeap heap) throws MyException {
        IValue leftVal = left.evaluate(symbolTable, heap);
        IValue rightVal = right.evaluate(symbolTable, heap);

        IType expectedType = new IntegerType();
        if (!leftVal.getType().equals(expectedType) || !rightVal.getType().equals(expectedType)) {
            throw new TypeMismatchException("Relational expressions require integer operands!");
        }

        IntegerValue leftInt = (IntegerValue) leftVal;
        IntegerValue rightInt = (IntegerValue) rightVal;

        return switch (operator) {
            case "<" -> new BooleanValue(leftInt.value() < rightInt.value());
            case "<=" -> new BooleanValue(leftInt.value() <= rightInt.value());
            case "==" -> new BooleanValue(leftInt.value() == rightInt.value());
            case "!=" -> new BooleanValue(leftInt.value() != rightInt.value());
            case ">" -> new BooleanValue(leftInt.value() > rightInt.value());
            case ">=" -> new BooleanValue(leftInt.value() >= rightInt.value());
            default -> throw new UndefinedOperatorException("Undefined relational operator: " + operator);
        };
    }

    @Override
    public IExpression deepCopy() {
        return new RelationalExpression(operator, left.deepCopy(), right.deepCopy());
    }

    @Override
    public IType typecheck(ISymbolTable<String, IType> typeEnv) throws MyException {
        IType type1 = left.typecheck(typeEnv);
        IType type2 = right.typecheck(typeEnv);

        //ambii operanzi trebuie să fie IntegerType
        if (!type1.equals(new IntegerType())) {
            throw new MyException("First operand of relational expression is not an integer.");
        }
        if (!type2.equals(new IntegerType())) {
            throw new MyException("Second operand of relational expression is not an integer.");
        }
        return new BooleanType();
    }

    @Override
    public String toString() {
        return left + " " + operator + " " + right;
    }
}
