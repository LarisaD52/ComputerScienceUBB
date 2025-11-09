package model.expression;

import exceptions.MyException;
import exceptions.TypeMismatchException;
import exceptions.UndefinedOperatorException;
import model.state.ISymbolTable;
import model.type.Type;
import model.value.BooleanValue;
import model.value.IValue;
import model.value.IntegerValue;

public record RelationalExpression(String operator, IExpression left, IExpression right) implements IExpression {

    @Override
    public IValue evaluate(ISymbolTable symbolTable) throws MyException {
        IValue leftVal = left.evaluate(symbolTable);
        IValue rightVal = right.evaluate(symbolTable);

        if (leftVal.getType() != Type.INTEGER || rightVal.getType() != Type.INTEGER) {
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
    public String toString() {
        return left + " " + operator + " " + right;
    }
}
